package com.mawen.learn.redis.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.CommandSuite;
import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.ISession;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.command.Session;
import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.persistence.RDBInputStream;
import com.mawen.learn.redis.basic.persistence.RDBOutputStream;
import com.mawen.learn.redis.basic.redis.RedisArray;
import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.RedisTokenType;
import com.mawen.learn.redis.basic.redis.RequestDecoder;
import com.mawen.learn.redis.basic.redis.SafeString;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static java.util.Collections.*;

/**
 * Java Redis Implementation
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDB implements ITinyDB, IServerContext {

	private static final Logger logger = Logger.getLogger(TinyDB.class.getName());

	private static final int INITIAL_SIZE = 1024;
	private static final int BUFFER_SIZE = INITIAL_SIZE * INITIAL_SIZE;
	private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;

	private static final int DEFAULT_DATABASES = 10;

	private final int port;
	private final String host;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private TinyDBInitializerHandler acceptHandler;
	private TinyDBConnectionHandler connectionHandler;
	private ChannelFuture future;

	private final CommandSuite commands = new CommandSuite();
	private final BlockingQueue<RedisArray> queue = new LinkedBlockingQueue<>();
	private final List<IDatabase> databases = new LinkedList<>();
	private final IDatabase admin = new Database(new HashMap<>());
	private final Map<String, ISession> clients = new HashMap<>();

	public TinyDB() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public TinyDB(String host, int port) {
		this(host, port, DEFAULT_DATABASES);
	}

	public TinyDB(String host, int port, int databases) {
		this.port = port;
		this.host = host;
		for (int i = 0; i < databases; i++) {
			this.databases.add(new Database(new HashMap<>()));
		}
	}

	public void start() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
		acceptHandler = new TinyDBInitializerHandler(this);
		connectionHandler = new TinyDBConnectionHandler(this);

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(acceptHandler)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_SNDBUF, BUFFER_SIZE)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

		future = bootstrap.bind(host, port);

		future.syncUninterruptibly();

		logger.info(() -> "server started: " + host + ":" + port);
	}

	public void stop() {
		try {
			if (future != null) {
				future.channel().close();
			}
		}
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

		clients.clear();
		queue.clear();
		admin.clear();

		logger.info("server stopped");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channel(SocketChannel channel) {
		logger.fine(() -> "new channel: " + sourceKey(channel));

		channel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast("linDelimiter", new RequestDecoder(MAX_FRAME_SIZE));
		channel.pipeline().addLast(connectionHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client connected: " + sourceKey);

		clients.put(sourceKey, new Session(sourceKey, ctx));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client disconnected: " + sourceKey);

		ISession session = clients.remove(sourceKey);
		if (session != null) {
			cleanSession(session);
		}
	}

	private void cleanSession(ISession session) {
		try {
			processCommand(new Request(this, session, safeString("unsubscribe"), emptyList()));
		}
		finally {
			session.destroy();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receive(ChannelHandlerContext ctx, RedisToken message) {
		String sourceKey = sourceKey(ctx.channel());

		logger.finest(() -> "message received: " + sourceKey);

		IRequest request = parseMessage(sourceKey, message);
		if (request != null) {
			processCommand(request);
		}
	}

	@Override
	public void publish(String sourceKey, String message) {
		ISession session = clients.get(sourceKey);
		if (session != null) {
			ByteBuf buffer = session.getContext().alloc().buffer(INITIAL_SIZE, BUFFER_SIZE);
			buffer.writeBytes(safeString(message).getBytes());
			session.getContext().writeAndFlush(buffer);
		}
	}

	@Override
	public IDatabase getAdminDatabase() {
		return admin;
	}

	@Override
	public IDatabase getDatabase(int i) {
		return databases.get(i);
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public int getClients() {
		return clients.size();
	}

	@Override
	public void exportRDB(OutputStream output) throws IOException {
		RDBOutputStream rdb = new RDBOutputStream(output);
		rdb.preamble(6);
		for (int i = 0; i < databases.size(); i++) {
			IDatabase db = databases.get(i);
			if (!db.isEmpty()) {
				rdb.select(i);
				rdb.database(db);
			}
		}
		rdb.end();
	}

	@Override
	public void importRDB(InputStream input) throws IOException {
		RDBInputStream rdb = new RDBInputStream(input);

		for (Map.Entry<Integer, IDatabase> entry : rdb.parse().entrySet()) {
			this.databases.set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public List<RedisArray> getCommands() {
		List<RedisArray> current = new LinkedList<>();
		queue.drainTo(current);
		return current;
	}

	@Override
	public ICommand getCommand(String name) {
		return commands.getCommand(name);
	}

	private IRequest parseMessage(String sourceKey, RedisToken message) {
		IRequest request = null;

		if (message.getType() == RedisTokenType.ARRAY) {
			request = parseArray(sourceKey, message);
		}
		else if (message.getType() == RedisTokenType.UNKNOWN) {
			request = parseLine(sourceKey, message);
		}
		return request;
	}

	private Request parseLine(String sourceKey, RedisToken message) {
		String command = message.getValue();
		String[] params = command.split(" ");
		String[] array = new String[params.length - 1];
		System.arraycopy(params, 1, array, 0, array.length);
		return new Request(this, clients.get(sourceKey), safeString(params[0]), safeAsList(array));
	}

	private Request parseArray(String sourceKey, RedisToken message) {
		List<SafeString> params = new LinkedList<>();
		for (RedisToken token : message.<RedisArray>getValue()) {
			params.add(token.getValue());
		}
		return new Request(this, clients.get(sourceKey), params.remove(0), params);
	}

	private void processCommand(IRequest request) {
		logger.fine(() -> "received command: " + request);

		ISession session = request.getSession();
		IDatabase db = databases.get(session.getCurrentDB());
		ICommand command = commands.getCommand(request.getCommand());
		if (command != null) {
			session.enqueue(() -> {
				try {
					Response response = new Response();
					command.execute(db, request, response);

					ByteBuf buffer = session.getContext().alloc().buffer(1024);
					buffer.writeBytes(response.getBytes());
					session.getContext().writeAndFlush(buffer);

					replication(request);

					if (response.isExit()) {
						session.getContext().close();
					}
				}
				catch (RuntimeException e) {
					logger.log(Level.SEVERE, "error executing command: " + request, e);
				}
			});
		}
		else {
			session.getContext().writeAndFlush("-ERR unknown command '" + request.getCommand() + "'");
		}
	}

	private void replication(IRequest request) {
		if (!admin.getOrDefault("slaves", DatabaseValue.set()).<Set<String>>getValue().isEmpty()) {
			queue.add(requestToArray(request));
		}
	}

	private RedisArray requestToArray(IRequest request) {
		RedisArray array = new RedisArray();
		// currentDB
		array.add(new RedisToken.IntegerRedisToken(request.getSession().getCurrentDB()));
		// command
		array.add(new RedisToken.StringRedisToken(safeString(request.getCommand())));
		// params
		for (SafeString safeStr : request.getSafeParams()) {
			array.add(new RedisToken.StringRedisToken(safeStr));
		}
		return array;
	}

	private String sourceKey(Channel channel) {
		InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
		return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
	}
}

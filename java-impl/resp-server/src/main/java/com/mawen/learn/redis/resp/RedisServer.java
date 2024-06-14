package com.mawen.learn.redis.resp;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.resp.command.CommandSuite;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.IServerContext;
import com.mawen.learn.redis.resp.command.ISession;
import com.mawen.learn.redis.resp.command.Request;
import com.mawen.learn.redis.resp.command.Response;
import com.mawen.learn.redis.resp.command.Session;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.RedisTokenType;
import com.mawen.learn.redis.resp.protocol.RequestDecoder;
import com.mawen.learn.redis.resp.protocol.SafeString;
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

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Objects.*;

/**
 * Java Redis Implementation
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisServer implements IRedis, IServerContext {

	private static final Logger logger = Logger.getLogger(RedisServer.class.getName());

	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;

	private static final int DEFAULT_DATABASES = 10;

	private final int port;
	private final String host;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	private ServerBootstrap bootstrap;

	private RedisInitializerHandler acceptHandler;
	private RedisConnectionHandler connectionHandler;

	private ChannelFuture future;

	protected final Map<String, Object> state = new HashMap<>();

	protected final Map<String, ISession> clients = new HashMap<>();

	protected final CommandSuite commands;


	public RedisServer(String host, int port, CommandSuite commands) {
		this.port = port;
		this.host = host;
		this.commands = requireNonNull(commands);
	}

	public void start() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
		acceptHandler = new RedisInitializerHandler(this);
		connectionHandler = new RedisConnectionHandler(this);

		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(acceptHandler)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_SNDBUF, BUFFER_SIZE)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

		// Bind and start to accept incoming connections.
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

		logger.info("server stopped");
	}

	@Override
	public void channel(SocketChannel channel) {
		logger.fine(() -> "new channel: " + sourceKey(channel));

		channel.pipeline().addLast("linDelimiter", new RequestDecoder(MAX_FRAME_SIZE));
		channel.pipeline().addLast(connectionHandler);
	}

	@Override
	public void connected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client connected: " + sourceKey);

		getSession(sourceKey, ctx);
	}

	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client disconnected: " + sourceKey);

		ISession session = clients.remove(sourceKey);
		if (session != null) {
			cleanSession(session);
		}
	}

	protected void cleanSession(ISession session) {

	}

	@Override
	public void receive(ChannelHandlerContext ctx, RedisToken message) {
		String sourceKey = sourceKey(ctx.channel());

		logger.finest(() -> "message received: " + sourceKey);

		IRequest request = parseMessage(sourceKey, message, getSession(sourceKey, ctx));
		if (request != null) {
			processCommand(request);
		}
	}

	private ISession getSession(String sourceKey, ChannelHandlerContext ctx) {
		ISession session = clients.getOrDefault(sourceKey, new Session(sourceKey, ctx));
		clients.putIfAbsent(sourceKey, session);
		return session;
	}

	protected void createSession(ISession session) {

	}

	private IRequest parseMessage(String sourceKey, RedisToken message, ISession session) {
		IRequest request = null;

		if (message.getType() == RedisTokenType.ARRAY) {
			request = parseArray(sourceKey, message, session);
		}
		else if (message.getType() == RedisTokenType.UNKNOWN) {
			request = parseLine(sourceKey, message, session);
		}
		return request;
	}

	private Request parseLine(String sourceKey, RedisToken message, ISession session) {
		String command = message.getValue();
		String[] params = command.split(" ");
		String[] array = new String[params.length - 1];
		System.arraycopy(params, 1, array, 0, array.length);
		return new Request(this, session, safeString(params[0]), safeAsList(array));
	}

	private Request parseArray(String sourceKey, RedisToken message, ISession session) {
		List<SafeString> params = new LinkedList<>();
		for (RedisToken token : message.<List<RedisToken>>getValue()) {
			params.add(token.getValue());
		}
		return new Request(this, session, params.remove(0), params);
	}

	protected void processCommand(IRequest request) {
		logger.fine(() -> "received command: " + request);

		IResponse response = new Response();
		ISession session = request.getSession();
		ICommand command = commands.getCommand(request.getCommand());

		if (command != null) {
			try {
				executeCommand(command, request, response);
			}
			catch (RuntimeException e) {
				logger.log(Level.SEVERE, "error executing command: " + request, e);
			}
		}
		else {
			writeResponse(session, response.addError("ERR unknown command '" + request.getCommand() + "'"));
		}
	}

	protected void executeCommand(ICommand command, IRequest request, IResponse response) {
		ISession session = request.getSession();
		command.execute(request, response);
		writeResponse(session, response);
		if (response.isExit()) {
			session.getContext().close();
		}
	}

	protected void writeResponse(ISession session, IResponse response) {
		session.getContext().writeAndFlush(responseToBuffer(session, response));
	}

	private ByteBuf responseToBuffer(ISession session, IResponse response) {
		byte[] array = ((Response)response).getBytes();
		return bytesToBuffer(session, array);
	}

	private ByteBuf bytesToBuffer(ISession session, byte[] array) {
		ByteBuf buffer = session.getContext().alloc().buffer(array.length);
		buffer.writeBytes(array);
		return buffer;
	}

	private String sourceKey(Channel channel) {
		InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
		return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
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
	public ICommand getCommand(String name) {
		return commands.getCommand(name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		return (T) state.get(key);
	}

	@Override
	public void putValue(String key, Object value) {
		state.put(key, value);
	}
}

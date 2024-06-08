package com.mawen.learn.redis.basic;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.CommandSuite;
import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.RedisTokenType;
import com.mawen.learn.redis.basic.redis.RequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
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

/**
 * Java Redis Implementation
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDB implements ITinyDB {

	private static final Logger logger = Logger.getLogger(TinyDB.class.getName());

	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;
	public static final int DEFAULT_PORT = 7081;
	public static final String DEFAULT_HOST = "localhost";

	private final int port;
	private final String host;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private TinyDBInitializerHandler acceptHandler;
	private TinyDBConnectionHandler connectionHandler;
	private ChannelFuture future;

	private final Map<String, ChannelHandlerContext> channels = new HashMap<>();
	private final CommandSuite commands = new CommandSuite();
	private final Database db = new Database(new ConcurrentHashMap<>());

	public TinyDB(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public TinyDB() {
		this(DEFAULT_HOST, DEFAULT_PORT);
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

		logger.info(() -> "adapter started: " + host + ":" + port);
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

		channels.clear();

		logger.info("adapter stopped");
	}

	@Override
	public void channel(SocketChannel channel) {
		logger.fine(() -> "new channel: " + sourceKey(channel));

		channel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast("linDelimiter", new RequestDecoder(MAX_FRAME_SIZE));
		channel.pipeline().addLast(connectionHandler);
	}

	@Override
	public void connected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client connected: " + sourceKey);

		channels.put(sourceKey, ctx);
	}

	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.fine(() -> "client disconnected: " + sourceKey);

		channels.remove(sourceKey);
	}

	@Override
	public void receive(ChannelHandlerContext ctx, RedisToken<?> message) {
		String sourceKey = sourceKey(ctx.channel());

		logger.info(() -> "message received: " + sourceKey);

		ctx.writeAndFlush(processCommand(parse(message)));
	}

	private IRequest parse(RedisToken<?> message) {
		IRequest request = null;

		if (message.getType() == RedisTokenType.ARRAY) {
			request = parseArray(message);
		}
		else if (message.getType() == RedisTokenType.UNKNOWN) {
			request = parseLine((message));
		}
		return request;
	}

	private Request parseLine(RedisToken<?> message) {
		logger.log(Level.SEVERE, "Received Unknown command");

		RedisToken.UnknownRedisToken unknownToken = (RedisToken.UnknownRedisToken) message;
		String command = unknownToken.getValue();
		String[] params = command.split(" ");
		String[] array = new String[params.length - 1];
		System.arraycopy(params, 1, array, 0, array.length);
		return new Request(params[0], Arrays.asList(array));
	}

	private Request parseArray(RedisToken<?> message) {
		RedisToken.ArrayRedisToken arrayToken = (RedisToken.ArrayRedisToken) message;
		List<String> params = new LinkedList<>();

		for (RedisToken<?> token : arrayToken.getValue()) {
			params.add(token.getValue().toString());
		}

		return new Request(params.get(0), params.subList(1, params.size()));
	}

	private String processCommand(IRequest request) {
		logger.fine(() -> "received command: " + request);

		IResponse response = new Response();
		ICommand command = commands.getCommand(request.getCommand());
		if (command != null) {
			command.execute(db, request, response);
		}
		else {
			response.addError("ERR unknown command '" + request.getCommand() + "'");
		}

		logger.info(() -> "Response is: " + response);

		return response.toString();
	}

	private String sourceKey(Channel channel) {
		InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
		return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
	}
}

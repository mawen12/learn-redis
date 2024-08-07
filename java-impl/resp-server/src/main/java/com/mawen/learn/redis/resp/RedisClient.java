package com.mawen.learn.redis.resp;

import java.util.logging.Logger;

import com.mawen.learn.redis.resp.protocol.RedisDecoder;
import com.mawen.learn.redis.resp.protocol.RedisEncoder;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import static java.util.Objects.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class RedisClient implements IRedis {

	private static final Logger logger = Logger.getLogger(RedisClient.class.getName());

	private static final String DELIMITER = "\r\n";

	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;

	private final String host;
	private final int port;

	private EventLoopGroup workerGroup;
	private Bootstrap bootstrap;

	private ChannelFuture future;

	private ChannelHandlerContext context;
	private RedisInitializerHandler acceptHandler;
	private RedisConnectionHandler connectionHandler;

	private final IRedisCallback callback;

	public RedisClient(String host, int port, IRedisCallback callback) {
		this.host = requireNonNull(host);
		this.port = requireRange(port, 1024, 65535);
		this.callback = requireNonNull(callback);
	}

	public void start() {
		workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		acceptHandler = new RedisInitializerHandler(this);
		connectionHandler = new RedisConnectionHandler(this);

		bootstrap = new Bootstrap()
				.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_SNDBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(acceptHandler);

		try {
			connect();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		try {
			if (future != null) {
				future.channel().close();
			}
		}
		finally {
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void channel(SocketChannel channel) {
		logger.info(() -> "connected to server: " + host + ":" + port);

		channel.pipeline().addLast("redisEncoder", new RedisEncoder());
		channel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast("linDelimiter", new RedisDecoder(MAX_FRAME_SIZE));
		channel.pipeline().addLast(connectionHandler);
	}

	@Override
	public void connected(ChannelHandlerContext ctx) {
		logger.info(() -> "channel active");

		this.context = ctx;

		callback.onConnect();
	}

	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		logger.info(() -> "client disconnected from server: " + host + ":" + port);

		if (this.context != null) {
			callback.onDisconnect();

			this.context = null;
		}
	}

	public void send(String message) {
		writeAndFlush(message + DELIMITER);
	}

	public void send(RedisToken message) {
		writeAndFlush(message);
	}

	@Override
	public void receive(ChannelHandlerContext ctx, RedisToken message) {
		callback.onMessage(message);
	}

	private void connect() {
		logger.info(() -> "trying to connect");

		future = bootstrap.connect(host, port);

		future.syncUninterruptibly();
	}

	private void writeAndFlush(Object message) {
		if (context != null) {
			context.writeAndFlush(message);
		}
	}

	private int requireRange(int value, int min, int max) {
		if (value <= min || value > max) {
			throw new IllegalArgumentException(min + " <= " + value + " < " + max);
		}
		return value;
	}
}

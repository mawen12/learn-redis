package com.mawen.learn.redis.basic;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.RequestDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class TinyDBClient implements ITinyDB {

	private static final Logger logger = Logger.getLogger(TinyDBClient.class.getName());

	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final int MAX_FRAME_SIZE = BUFFER_SIZE * 100;

	private final String host;
	private final int port;
	private final int reconnectionTime;

	private EventLoopGroup workerGroup;
	private Bootstrap bootstrap;

	private ChannelFuture future;

	private ChannelHandlerContext ctx;
	private TinyDBInitializerHandler initHandler;
	private TinyDBConnectionHandler connectionHandler;

	private final ITinyDBCallback callback;

	public TinyDBClient(ITinyDBCallback callback) {
		this("localhost", 7081, callback);
	}

	public TinyDBClient(String host, int port, ITinyDBCallback callback) {
		this.host = host;
		this.port = port;
		this.reconnectionTime = 10;
		this.callback = callback;
	}

	public void start() {
		workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		initHandler = new TinyDBInitializerHandler(this);
		connectionHandler = new TinyDBConnectionHandler(this);

		bootstrap = new Bootstrap()
				.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_RCVBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_SNDBUF, BUFFER_SIZE)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(initHandler);

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

	private void connect() {
		logger.info(() -> "trying to connect");

		this.future = bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
			if (!future.isSuccess()) {
				future.channel().close();

				workerGroup.schedule(this::connect, reconnectionTime, TimeUnit.SECONDS);
			}
			else {
				logger.info(() -> "successful connection");
			}
		});
	}

	@Override
	public void channel(SocketChannel channel) {
		logger.fine(() -> "connected to server");

		channel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast("linDelimiter", new RequestDecoder(MAX_FRAME_SIZE));
		channel.pipeline().addLast(connectionHandler);
	}

	@Override
	public void connected(ChannelHandlerContext ctx) {
		logger.info(() -> "channel active");

		this.ctx = ctx;

		callback.onConnect();
	}

	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		if (this.ctx != null) {
			logger.info(() -> "client disconnected from server");

			this.ctx = null;

			callback.onDisconnect();

			// reconnect
			connect();
		}
	}

	public void send(String message) {
		if (this.ctx != null) {
			this.ctx.writeAndFlush(message);
		}
	}

	@Override
	public void receive(ChannelHandlerContext ctx, RedisToken message) {
		callback.onMessage(message);
	}
}

package com.mawen.learn.redis.basic;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.command.impl.EchoCommand;
import com.mawen.learn.redis.basic.command.impl.GetCommand;
import com.mawen.learn.redis.basic.command.impl.PingCommand;
import com.mawen.learn.redis.basic.command.impl.SetCommand;
import com.mawen.learn.redis.basic.data.Database;
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
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
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
	private static final int DEFAULT_PORT = 7081;
	private static final String DEFAULT_HOST = "localhost";

	private final int port;
	private final String host;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private TinyDBInitializerHandler acceptHandler;
	private TinyDBConnectionHandler connectionHandler;
	private ChannelFuture future;

	private final Map<String, ChannelHandlerContext> channels = new HashMap<>();
	private final Map<String, ICommand> commands = new HashMap<>();
	private final Database db = new Database();

	public TinyDB(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public TinyDB() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public void init() {
		commands.put("ping", new PingCommand());
		commands.put("set", new SetCommand());
		commands.put("get", new GetCommand());
		commands.put("echo", new EchoCommand());
	}

	public void start() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
		acceptHandler = new TinyDBInitializerHandler(this);
		connectionHandler = new TinyDBConnectionHandler(this);

		try {
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
		}
		catch (RuntimeException e) {
			throw new TinyDBException(e);
		}

		logger.log(Level.INFO, "adapter started");
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

		logger.log(Level.INFO, "adapter stopped");
	}

	@Override
	public void channel(SocketChannel channel) {
		logger.log(Level.INFO, "new channel: {0}", sourceKey(channel));

		channel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast("linDelimiter", new DelimiterBasedFrameDecoder(MAX_FRAME_SIZE, true, Delimiters.lineDelimiter()));
		channel.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
		channel.pipeline().addLast(connectionHandler);
	}

	@Override
	public void connected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.log(Level.INFO, "client connected: {0}", sourceKey);

		channels.put(sourceKey, ctx);
	}

	@Override
	public void disconnected(ChannelHandlerContext ctx) {
		String sourceKey = sourceKey(ctx.channel());

		logger.log(Level.INFO, "client disconnected: {0}", sourceKey);

		channels.remove(sourceKey);
	}

	@Override
	public void receive(ChannelHandlerContext ctx, String message) {
		String sourceKey = sourceKey(ctx.channel());

		logger.log(Level.INFO, "message received: {0}", sourceKey);

		ctx.writeAndFlush(processCommand(parse(message)));
	}

	private IRequest parse(String message) {
		Request request = new Request();
		String[] params = message.split(" ");
		request.setCommand(params[0]);
		request.setParams(Arrays.asList(params));
		return request;
	}

	private String processCommand(IRequest request) {
		String cmd = request.getCommand();
		logger.log(Level.INFO, "command: {0}", request.getParams());

		IResponse response = new Response();
		ICommand command = commands.get(cmd);
		if (command != null) {
			command.execute(db, request, response);
		}
		else {
			response.addError("ERR unknown command '" + cmd + "'");
		}
		return response.toString();
	}

	private String sourceKey(Channel channel) {
		InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
		return remoteAddress.getHostName() + ":" + remoteAddress.getPort();
	}
}

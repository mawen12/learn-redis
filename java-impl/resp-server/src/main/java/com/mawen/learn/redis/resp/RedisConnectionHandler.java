package com.mawen.learn.redis.resp;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Netty channel handler
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisConnectionHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(RedisConnectionHandler.class.getName());

	private final IRedis impl;

	public RedisConnectionHandler(IRedis impl) {
		this.impl = impl;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		impl.connected(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			impl.receive(ctx, (RedisToken) msg);
		}
		finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.log(Level.SEVERE, "channel inactive");
		impl.disconnected(ctx);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.SEVERE, "uncaught exception", cause);
		impl.disconnected(ctx);
		ctx.close();
	}
}

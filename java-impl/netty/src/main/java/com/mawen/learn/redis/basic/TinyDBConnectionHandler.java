package com.mawen.learn.redis.basic;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.redis.RedisToken;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Netty channel handler
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDBConnectionHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TinyDBConnectionHandler.class.getName());

	private final ITinyDB tinyDB;

	public TinyDBConnectionHandler(ITinyDB tinyDB) {
		this.tinyDB = tinyDB;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		tinyDB.connected(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			tinyDB.receive(ctx, (RedisToken) msg);
		}
		finally {
			ReferenceCountUtil.release(msg);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.log(Level.SEVERE, "channel inactive");
		tinyDB.disconnected(ctx);
		ctx.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.SEVERE, "uncaught exception", cause);
		tinyDB.disconnected(ctx);
		ctx.close();
	}
}

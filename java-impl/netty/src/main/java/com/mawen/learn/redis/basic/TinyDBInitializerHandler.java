package com.mawen.learn.redis.basic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDBInitializerHandler extends ChannelInitializer<SocketChannel> {

	private final ITinyDB tinyDB;

	public TinyDBInitializerHandler(ITinyDB tinyDB) {
		this.tinyDB = tinyDB;
	}

	/**
	 * Method called when the connection is established
	 */
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		tinyDB.channel(socketChannel);
	}

	/**
	 * Method called when the channel is inactive
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		tinyDB.disconnected(ctx);
	}
}

package com.mawen.learn.redis.resp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Netty initialization handler
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisInitializerHandler extends ChannelInitializer<SocketChannel> {

	private final IRedis impl;

	public RedisInitializerHandler(IRedis impl) {
		this.impl = impl;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		impl.channel(socketChannel);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		impl.disconnected(ctx);
	}
}

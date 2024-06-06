package com.mawen.learn.redis.basic;

import com.mawen.learn.redis.basic.redis.RedisToken;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface ITinyDB {

	/**
	 * Method called when creating a new channel
	 */
	void channel(SocketChannel channel);

	/**
	 * Method called when the connection has been established and is active
	 */
	void connected(ChannelHandlerContext ctx);

	/**
	 * Method called when the connection is lost
	 */
	void disconnected(ChannelHandlerContext ctx);

	/**
	 * Method called when a message is received
	 */
	void receive(ChannelHandlerContext ctx, RedisToken<?> message);
}

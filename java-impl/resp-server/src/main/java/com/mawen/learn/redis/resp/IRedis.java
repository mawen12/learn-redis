package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

/**
 * Server interface
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IRedis {

	/**
	 * When a new channel is created, and the server has to prepare the pipeline
	 */
	void channel(SocketChannel channel);

	/**
	 * When a new client is connected
	 */
	void connected(ChannelHandlerContext ctx);

	/**
	 * When a client is disconnected
	 */
	void disconnected(ChannelHandlerContext ctx);

	/**
	 * When a message is received
	 */
	void receive(ChannelHandlerContext ctx, RedisToken message);
}

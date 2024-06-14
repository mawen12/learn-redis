package com.mawen.learn.redis.resp.command;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface ISession {

	String getId();

	ChannelHandlerContext getContext();

	void destroy();

	<T> T getValue(String key);

	void putValue(String key, Object value);
}

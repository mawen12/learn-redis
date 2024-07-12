package com.mawen.learn.redis.resp.command;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface ISession {

	String getId();

	void publish(RedisToken msg);

	void close();

	void destroy();

	<T> T getValue(String key);

	void putValue(String key, Object value);

	<T> T removeValue(String key);
}
package com.mawen.learn.redis.basic;

import com.mawen.learn.redis.basic.redis.RedisToken;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public interface ITinyDBCallback {

	void onConnect();

	void onDisconnect();

	void onMessage(RedisToken token);
}

package com.mawen.learn.redis.resp;


import com.mawen.learn.redis.resp.protocol.RedisToken;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public interface IRedisCallback {

	void onConnect();

	void onDisconnect();

	void onMessage(RedisToken token);
}

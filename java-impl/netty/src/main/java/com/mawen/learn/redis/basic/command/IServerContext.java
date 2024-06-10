package com.mawen.learn.redis.basic.command;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface IServerContext {

	void publish(String destination, String message);
}

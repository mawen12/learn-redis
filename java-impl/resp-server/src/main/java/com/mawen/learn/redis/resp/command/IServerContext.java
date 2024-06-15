package com.mawen.learn.redis.resp.command;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface IServerContext {

	int getPort();

	int getClients();

	ICommand getCommand(String name);

	<T> T getValue(String key);

	void putValue(String key, Object value);

	<T> T removeValue(String key);
}

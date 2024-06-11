package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface IServerContext {

	int getPort();

	int getClients();

	void publish(String destination, String message);

	IDatabase getDatabase();
}

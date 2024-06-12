package com.mawen.learn.redis.basic.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.RedisArray;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface IServerContext {

	int getPort();

	int getClients();

	void publish(String destination, String message);

	IDatabase getAdminDatabase();

	IDatabase getDatabase(int i);

	void exportRDB(OutputStream output) throws IOException;

	void importRDB(InputStream input) throws IOException;

	List<RedisArray> getCommands();

	ICommand getCommand(String name);
}

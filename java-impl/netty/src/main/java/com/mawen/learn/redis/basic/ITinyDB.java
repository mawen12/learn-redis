package com.mawen.learn.redis.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.command.IServerContext;
import com.mawen.learn.redis.resp.protocol.RedisToken;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public interface ITinyDB extends IServerContext {

	int DEFAULT_PORT = 7081;
	String DEFAULT_HOST = "localhost";

	boolean isMaster();

	void setMaster(boolean master);

	void importRDB(InputStream input) throws IOException;

	void exportRDB(OutputStream output) throws IOException;

	IDatabase getDatabase(int i);

	IDatabase getAdminDatabase();

	void publish(String sourceKey, RedisToken message);

	List<List<RedisToken>> getCommandsToReplicate();
}

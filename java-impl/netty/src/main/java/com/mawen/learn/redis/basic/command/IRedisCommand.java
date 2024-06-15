package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDBServerState;
import com.mawen.learn.redis.basic.TinyDBSessionState;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.IServerContext;
import com.mawen.learn.redis.resp.command.ISession;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public interface IRedisCommand {

	void execute(IDatabase db, IRequest request, IResponse response);

	default ITinyDB getTinyDB(IServerContext server) {
		return (ITinyDB) server;
	}

	default IDatabase getAdminDatabase(IServerContext server) {
		return getServerState(server).getAdminDatabase();
	}

	default TinyDBServerState getServerState(IServerContext server) {
		return server.getValue("state");
	}

	default TinyDBSessionState getSessionState(ISession session) {
		return session.getValue("state");
	}
}

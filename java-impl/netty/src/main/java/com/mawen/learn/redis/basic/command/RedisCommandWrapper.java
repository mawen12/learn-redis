package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.RedisServerState;
import com.mawen.learn.redis.basic.RedisSessionState;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.PubSubAllowed;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class RedisCommandWrapper implements ICommand {

	private int params;

	private DataType dataType;

	private final boolean pubSubAllowed;

	private final IRedisCommand command;

	public RedisCommandWrapper(IRedisCommand command) {
		this.command = command;
		ParamLength length = command.getClass().getAnnotation(ParamLength.class);
		if (length != null) {
			this.params = length.value();
		}

		ParamType type = command.getClass().getAnnotation(ParamType.class);
		if (type != null) {
			this.dataType = type.value();
		}

		this.pubSubAllowed = command.getClass().isAnnotationPresent(PubSubAllowed.class);
	}

	@Override
	public void execute(IRequest request, IResponse response) {
		IDatabase db = getCurrentDB(request);
		if (request.getLength() < params) {
			response.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
		}
		else if (dataType != null && !db.isType(DatabaseKey.safeKey(request.getParam(0)), dataType)) {
			response.addError("WRONGTYPE Operation against a key holding the wrong kind of value");
		}
		else if (isSubscribed(request) && !pubSubAllowed) {
			response.addError("ERR only (P)SUBSCRIBE / (P)UNSUBSCRIBE / QUIT allowed in this context");
		}
		else {
			command.execute(db, request, response);
		}
	}

	private IDatabase getCurrentDB(IRequest request) {
		RedisServerState serverState = getServerState(request);
		RedisSessionState sessionState = getSessionState(request);
		return serverState.getDatabase(sessionState.getCurrentDB());
	}

	private RedisSessionState getSessionState(IRequest request) {
		return request.getSession().getValue("state");
	}

	private RedisServerState getServerState(IRequest request) {
		return request.getServerContext().getValue("state");
	}

	private boolean isSubscribed(IRequest request) {
		return !getSessionState(request).getSubscriptions().isEmpty();
	}
}

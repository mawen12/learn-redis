package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.PubSubAllowed;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class CommandWrapper implements ICommand {

	private final ICommand command;
	private DataType dataType;
	private final boolean pubSubAllowed;
	private int params;

	public CommandWrapper(ICommand command) {
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
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (request.getLength() < params) {
			response.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
		}
		else if (dataType != null && !db.isType(request.getParam(0), dataType)) {
			response.addError("WRONGTYPE Operation against a key holding the wrong kind of value");
		}
		else if (isSubscribed(request) && !pubSubAllowed) {
			response.addError("ERR only (P)SUBSCRIBE / (P)UNSUBSCRIBE / QUIT allowed in this context");
		}
		else {
			command.execute(db, request, response);
		}
	}

	private boolean isSubscribed(IRequest request) {
		return !request.getSession().getSubscriptions().isEmpty();
	}
}

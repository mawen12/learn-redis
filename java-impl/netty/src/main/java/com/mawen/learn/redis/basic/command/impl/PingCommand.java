package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class PingCommand implements ICommand {

	private static final String PONG = "PONG";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (request.getLength() > 1) {
			response.addBulkStr(request.getParam(1));
		}
		else {
			response.addSimpleStr(PONG);
		}
	}
}

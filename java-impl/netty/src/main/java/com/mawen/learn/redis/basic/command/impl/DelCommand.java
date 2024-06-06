package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DelCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		db.remove(request.getParam(0));
		response.addSimpleStr(OK);
	}
}

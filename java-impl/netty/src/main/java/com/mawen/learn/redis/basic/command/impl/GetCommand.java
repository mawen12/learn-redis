package com.mawen.learn.redis.basic.command.impl;

import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class GetCommand implements ICommand {

	private static final Logger logger = Logger.getLogger(GetCommand.class.getName());

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		response.addValue(db.get(request.getParam(0)));
	}
}

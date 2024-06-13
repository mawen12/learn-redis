package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/11
 */
@ReadOnly
@Command("select")
@ParamLength(1)
public class SelectCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			request.getSession().setCurrentDB(Integer.parseInt(request.getParam(0)));
			response.addSimpleStr(RESULT_OK);
		}
		catch (NumberFormatException e) {
			response.addError("ERR invalid DB index");
		}
	}
}

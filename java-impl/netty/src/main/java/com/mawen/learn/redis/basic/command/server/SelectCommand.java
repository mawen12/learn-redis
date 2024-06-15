package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

import static java.lang.Integer.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/11
 */
@ReadOnly
@Command("select")
@ParamLength(1)
public class SelectCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			getSessionState(request.getSession()).setCurrentDB(parseCurrentDB(request));
			response.addSimpleStr(IResponse.RESULT_OK);
		}
		catch (NumberFormatException e) {
			response.addError("ERR invalid DB index");
		}
	}

	private int parseCurrentDB(IRequest request) {
		return parseInt(request.getParam(0).toString());
	}
}

package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
@Command("rename")
@ParamLength(2)
public class RenameCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (db.rename(request.getParam(0), request.getParam(1))) {
			response.addSimpleStr(RESULT_OK);
		}
		else {
			response.addError("ERR no such key");
		}
	}
}

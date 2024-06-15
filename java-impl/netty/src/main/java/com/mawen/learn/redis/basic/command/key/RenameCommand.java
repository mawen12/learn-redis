package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
@Command("rename")
@ParamLength(2)
public class RenameCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (db.rename(safeKey(request.getParam(0)), safeKey(request.getParam(1)))) {
			response.addSimpleStr(IResponse.RESULT_OK);
		}
		else {
			response.addError("ERR no such key");
		}
	}
}

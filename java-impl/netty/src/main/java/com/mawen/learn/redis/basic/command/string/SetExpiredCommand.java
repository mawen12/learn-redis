package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.IRedisCommand;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
@Command("setex")
@ParamLength(3)
public class SetExpiredCommand implements IRedisCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			db.put(safeKey(request.getParam(0), parseTtl(request.getParam(1))), string(request.getParam(2)));
			response.addSimpleStr(IResponse.RESULT_OK);
		}
		catch (Exception e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}

	private int parseTtl(SafeString safeString) {
		return Integer.parseInt(safeString.toString());
	}
}

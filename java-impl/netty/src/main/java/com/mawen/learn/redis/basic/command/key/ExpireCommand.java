package com.mawen.learn.redis.basic.command.key;


import com.mawen.learn.redis.basic.command.IRedisCommand;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
@Command("expire")
@ParamLength(2)
public class ExpireCommand implements IRedisCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseKey key = db.overrideKey(safeKey(request.getParam(0), parseTtl(request.getParam(1))));
			response.addInt(key != null);
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}

	private int parseTtl(SafeString param) {
		return Integer.parseInt(param.toString());
	}
}

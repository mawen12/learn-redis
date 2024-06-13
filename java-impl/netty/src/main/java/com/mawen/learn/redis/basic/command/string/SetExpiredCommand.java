package com.mawen.learn.redis.basic.command.string;

import java.util.concurrent.TimeUnit;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
@Command("setex")
@ParamLength(3)
public class SetExpiredCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		db.put(ttlKey(request.getParam(0), parseTtl(request.getParam(1))), string(request.getParam(2)));
		response.addSimpleStr(RESULT_OK);
	}

	private long parseTtl(SafeString safeString) {
		return TimeUnit.SECONDS.toMillis(Integer.parseInt(safeString.toString()));
	}
}

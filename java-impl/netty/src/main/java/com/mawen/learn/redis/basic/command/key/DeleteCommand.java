package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.IRedisCommand;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("del")
@ParamLength(1)
public class DeleteCommand implements IRedisCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		int removed = 0;
		for (SafeString key : request.getParams()) {
			DatabaseValue value = db.remove(safeKey(key));
			if (value != null) {
				removed += 1;
			}
		}

		response.addInt(removed);
	}
}

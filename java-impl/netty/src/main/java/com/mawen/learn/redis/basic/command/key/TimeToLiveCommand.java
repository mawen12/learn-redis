package com.mawen.learn.redis.basic.command.key;

import java.util.concurrent.TimeUnit;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
@Command("ttl")
@ParamLength(1)
public class TimeToLiveCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseKey key = db.getKey(DatabaseKey.safeKey(request.getParam(0)));
		if (key != null) {
			response.addInt(TimeUnit.MICROSECONDS.toSeconds(key.timeToLive()));
		}
		else {
			response.addInt(-2);
		}
	}
}

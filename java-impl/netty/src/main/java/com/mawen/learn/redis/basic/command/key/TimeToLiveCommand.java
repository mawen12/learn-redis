package com.mawen.learn.redis.basic.command.key;

import java.util.concurrent.TimeUnit;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
@Command("ttl")
@ParamLength(1)
public class TimeToLiveCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseKey key = db.getKey(DatabaseKey.safeKey(request.getParam(0)));
		if (key != null) {
			response.addInt(seconds(key));
		}
		else {
			response.addInt(-2);
		}
	}

	private int seconds(DatabaseKey key) {
		return (int) TimeUnit.MICROSECONDS.toSeconds(key.timeToLive());
	}
}

package com.mawen.learn.redis.resp.command.server;

import java.util.List;

import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.RedisToken;

import static com.mawen.learn.redis.resp.protocol.RedisToken.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("time")
public class TimeCommand implements ICommand {

	private static final int SCALE = 1000;

	@Override
	public void execute(IRequest request, IResponse response) {
		long currentTimeMillis = System.currentTimeMillis();
		List<RedisToken> result = asList(string(seconds(currentTimeMillis)), string(microseconds(currentTimeMillis)));
		response.addArray(result);
	}

	private String seconds(long currentTimeMillis) {
		return String.valueOf(currentTimeMillis / SCALE);
	}

	private String microseconds(long currentTimeMillis) {
		return String.valueOf((currentTimeMillis % SCALE) * SCALE);
	}
}

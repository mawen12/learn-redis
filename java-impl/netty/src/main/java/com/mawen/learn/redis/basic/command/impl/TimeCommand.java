package com.mawen.learn.redis.basic.command.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TimeCommand implements ICommand {

	private static final int SCALE = 1000;

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		long currentTimeMillis = System.currentTimeMillis();
		List<String> result = Stream.of(seconds(currentTimeMillis), microseconds(currentTimeMillis))
				.collect(Collectors.toList());
		response.addArray(result);
	}

	private String seconds(long currentTimeMillis) {
		return String.valueOf(currentTimeMillis / SCALE);
	}

	private String microseconds(long currentTimeMillis) {
		return String.valueOf((currentTimeMillis % SCALE) * SCALE);
	}
}

package com.mawen.learn.redis.basic.command.set;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@Command("sinter")
@ParamLength(2)
@ParamType(DataType.SET)
public class SetIntersectionCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue first = db.getOrDefault(request.getParam(0), DatabaseValue.set());
		Set<String> result = new HashSet<>(first.<Set<String>>getValue());

		for (String param : request.getParams().stream().skip(1).collect(Collectors.toList())) {
			result.retainAll(db.getOrDefault(param, DatabaseValue.set()).<Set<String>>getValue());
		}

		response.addArray(result);
	}
}

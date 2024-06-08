package com.mawen.learn.redis.basic.command.set;

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

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("sadd")
@ParamLength(2)
@ParamType(DataType.SET)
public class SetAddCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		Set<String> values = request.getParams().stream().skip(1).collect(Collectors.toSet());

		DatabaseValue value = db.merge(request.getParam(0), set(values), (oldValue, newValue) -> {
			Set<String> oldSet = oldValue.getValue();
			Set<String> newSet = newValue.getValue();
			oldSet.addAll(newSet);
			return oldValue;
		});

		response.addInt(value.<Set<String>>getValue().size());
	}
}

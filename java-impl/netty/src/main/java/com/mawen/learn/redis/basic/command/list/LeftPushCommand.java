package com.mawen.learn.redis.basic.command.list;

import java.util.List;
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
@Command("lpush")
@ParamLength(2)
@ParamType(DataType.LIST)
public class LeftPushCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<String> values = request.getParams().stream().skip(1).collect(Collectors.toList());

		DatabaseValue result = db.merge(request.getParam(0), list(values), (oldValue, newValue) -> {
			List<String> oldList = oldValue.getValue();
			List<String> newList = newValue.getValue();
			oldList.addAll(0, newList);
			return oldValue;
		});

		response.addInt(result.<List<String>>getValue().size());
	}
}

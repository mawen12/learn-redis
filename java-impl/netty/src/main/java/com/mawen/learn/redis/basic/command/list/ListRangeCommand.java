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
 * @since 2024/6/10
 */
@Command("lrange")
@ParamLength(3)
@ParamType(DataType.LIST)
public class ListRangeCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.getOrDefault(request.getParam(0), EMPTY_LIST);
			List<String> list = value.getValue();

			int from = Integer.parseInt(request.getParam(1));
			if (from < 0) {
				from += list.size();
			}
			int to = Integer.parseInt(request.getParam(2));
			if (to < 0) {
				to += list.size();
			}

			int min = Math.min(from, to);
			int max = Math.max(from, to);

			List<String> result = list.stream().skip(min).limit((max - min) + 1).collect(Collectors.toList());

			response.addArray(result);
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}
}

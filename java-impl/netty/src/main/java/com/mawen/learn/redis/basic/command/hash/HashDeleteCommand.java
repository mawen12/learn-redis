package com.mawen.learn.redis.basic.command.hash;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
 * @since 2024/6/7
 */
@Command("hdel")
@ParamLength(2)
@ParamType(DataType.HASH)
public class HashDeleteCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<String> keys = request.getParams().stream().skip(1).collect(Collectors.toList());

		DatabaseValue value = db.getOrDefault(request.getParam(0), hash());

		List<String> removedKeys = new LinkedList<>();
		Map<String, String> map = value.getValue();
		for (String key : keys) {
			String data = map.remove(key);
			if (data != null) {
				removedKeys.add(key);
			}
		}

		response.addInt(!removedKeys.isEmpty());
	}
}

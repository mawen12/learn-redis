package com.mawen.learn.redis.basic.command.impl;

import java.util.HashMap;
import java.util.Map;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@ParamLength(3)
@ParamType(DataType.HASH)
public class HashSetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = new DatabaseValue(DataType.HASH);

		HashMap<String, String> map = new HashMap<>();
		map.put(request.getParam(1), request.getParam(2));

		value.setValue(map);

		DatabaseValue resultValue = db.merge(request.getParam(0), value, (oldValue, newValue) -> {
			if (oldValue != null) {
				Map<Object, Object> oldMap = oldValue.getValue();
				Map<Object, Object> newMap = newValue.getValue();
				oldMap.putAll(newMap);
				return oldValue;
			}
			return newValue;
		});

		Map<String, String> resultMap = resultValue.getValue();

		response.addInt(resultMap.get(request.getParam(1)) == null);

	}
}

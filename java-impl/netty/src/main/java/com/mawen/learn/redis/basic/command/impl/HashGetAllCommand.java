package com.mawen.learn.redis.basic.command.impl;

import java.util.ArrayList;
import java.util.List;
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
@ParamLength(1)
@ParamType(DataType.HASH)
public class HashGetAllCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = db.get(request.getParam(0));
		if (value != null) {
			List<String> result = new ArrayList<>();
			Map<String, String> map = value.getValue();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				result.add(entry.getKey());
				result.add(entry.getValue());
			}
			response.addArray(result);
		}
		else {
			response.addArray(null);
		}
	}
}

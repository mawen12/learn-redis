package com.mawen.learn.redis.basic.command.hash;

import java.util.HashMap;
import java.util.Map;

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
 * @since 2024/6/6
 */
@Command("hset")
@ParamLength(3)
@ParamType(DataType.HASH)
public class HashSetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = hash(entry(request.getParam(1), request.getParam(2)));

		DatabaseValue resultValue = db.merge(request.getParam(0), value, (oldValue, newValue) -> {
			Map<String, String> merge = new HashMap<>();
			merge.putAll(oldValue.getValue());
			merge.putAll(newValue.getValue());
			return hash(merge.entrySet());
		});

		Map<String, String> resultMap = resultValue.getValue();

		response.addInt(resultMap.get(request.getParam(1)) == null);
	}
}
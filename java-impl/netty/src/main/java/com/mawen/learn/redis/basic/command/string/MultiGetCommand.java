package com.mawen.learn.redis.basic.command.string;

import java.util.ArrayList;
import java.util.List;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("mget")
@ParamLength(1)
public class MultiGetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<DatabaseValue> result = new ArrayList<>(request.getLength());
		for (String key : request.getParams()) {
			result.add(db.get(key));
		}
		response.addArrayValue(result);
	}
}

package com.mawen.learn.redis.basic.command.string;

import java.util.ArrayList;
import java.util.List;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@ReadOnly
@Command("mget")
@ParamLength(1)
public class MultiGetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<DatabaseValue> result = new ArrayList<>(request.getLength());
		for (DatabaseKey key : request.getParams().stream().map(DatabaseKey::safeKey).collect(toList())) {
			result.add(db.get(key));
		}
		response.addArrayValue(result);
	}
}

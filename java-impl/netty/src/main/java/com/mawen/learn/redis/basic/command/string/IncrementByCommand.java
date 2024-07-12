package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("incrby")
@ParamLength(2)
@ParamType(DataType.STRING)
public class IncrementByCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.merge(safeKey(request.getParam(0)), string(request.getParam(1)), (oldValue, newValue) -> {
				int increment = Integer.parseInt(newValue.getValue().toString());
				int current = Integer.parseInt(oldValue.getValue().toString());
				return string(String.valueOf(current + increment));
			});

			response.addInt(Integer.parseInt(value.getValue().toString()));
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}
}

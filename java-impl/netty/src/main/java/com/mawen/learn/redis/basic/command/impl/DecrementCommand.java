package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
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
@ParamLength(1)
@ParamType(DataType.STRING)
public class DecrementCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.merge(
					request.getParam(0),
					string("-1"),
					(oldValue, newValue) -> {
						if (oldValue != null) {
							oldValue.decrementAndGet(1);
							return oldValue;
						}
						return newValue;
					});

			response.addInt(value.getValue());
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}
}

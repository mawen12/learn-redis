package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DecrementByCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = new DatabaseValue();
			value.setType(DataType.STRING);
			value.setValue("-1");

			value = db.merge(request.getParam(0), value, (oldValue, newValue) -> {
				if (oldValue != null) {
					int decrement = Integer.parseInt(request.getParam(1));
					oldValue.decrementAndGet(decrement);
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

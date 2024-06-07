package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
@ParamLength(2)
public class MultiSetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		String key = null;
		for (String value : request.getParams()) {
			if (key != null) {
				db.merge(key, string(value), (oldValue, newValue) -> newValue);
				key = null;
			}
			else {
				key = value;
			}
		}
		response.addSimpleStr("OK");
	}
}

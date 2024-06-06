package com.mawen.learn.redis.basic.command.impl;

import java.util.Map;

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
public class HashGetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = db.get(request.getParam(0));
		if (value != null) {
			if (value.getType() == DataType.HASH) {
				Map<String, String> map = value.getValue();
				response.addBulkStr(map.get(request.getParam(1)));
			}
			else {
				response.addError("WRONGTYPE Operation against a key holding the wrong kind of value");
			}
		}
		else {
			response.addBulkStr(null);
		}
	}
}

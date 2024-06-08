package com.mawen.learn.redis.basic.command.impl;

import java.util.List;

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
 * @since 2024/6/8
 */
@ParamLength(1)
@ParamType(DataType.LIST)
public class LeftPopCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = db.get(request.getParam(0));

		if (value != null) {
			List<String> list = value.getValue();
			response.addBulkStr(list.remove(0));
		}
		else {
			response.addBulkStr(null);
		}
	}
}

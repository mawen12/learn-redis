package com.mawen.learn.redis.basic.command.list;

import java.util.List;

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
 * @since 2024/6/8
 */
@Command("rpop")
@ParamLength(1)
@ParamType(DataType.LIST)
public class RightPopCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = db.getOrDefault(request.getParam(0), list());

		if (value != null) {
			List<String> list = value.getValue();
			// XXX: must be an atomic operation
			response.addBulkStr(list.remove((list.size() - 1)));
		}
		else {
			response.addBulkStr(null);
		}
	}
}

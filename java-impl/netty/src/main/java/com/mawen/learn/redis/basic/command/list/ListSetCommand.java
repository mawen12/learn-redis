package com.mawen.learn.redis.basic.command.list;

import java.util.ArrayList;
import java.util.List;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@Command("lset")
@ParamLength(3)
@ParamType(DataType.LIST)
public class ListSetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			int index = Integer.parseInt(request.getParam(1));
			db.merge(request.getParam(0), EMPTY_LIST, (oldValue, newValue) -> {
				List<String> merge = new ArrayList<>(oldValue.<List<String>>getValue());
				merge.set(index > -1 ? index : merge.size() + index, request.getParam(2));
				return list(merge);
			});

			response.addSimpleStr("OK");
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
		catch (IndexOutOfBoundsException e) {
			response.addError("ERR index out of range");
		}
	}
}

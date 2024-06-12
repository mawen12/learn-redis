package com.mawen.learn.redis.basic.command.list;

import java.util.LinkedList;
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
import static com.mawen.learn.redis.basic.redis.SafeString.*;

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
		List<String> removed = new LinkedList<>();

		db.merge(request.getParam(0), list(), (oldValue, newValue) -> {
			List<String> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			if (!merge.isEmpty()) {
				removed.add(merge.remove(merge.size() - 1));
			}
			return list(merge);
		});

		if (removed.isEmpty()) {
			response.addBulkStr(null);
		}
		else {
			response.addBulkStr(safeString(removed.remove(0)));
		}
	}
}

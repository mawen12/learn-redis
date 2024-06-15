package com.mawen.learn.redis.basic.command.list;

import java.util.LinkedList;
import java.util.List;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("lpop")
@ParamLength(1)
@ParamType(DataType.LIST)
public class LeftPopCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<SafeString> removed = new LinkedList<>();

		db.merge(safeKey(request.getParam(0)), EMPTY_LIST, (oldValue, newValue) -> {
			List<SafeString> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			if (!merge.isEmpty()) {
				removed.add(merge.remove(0));
			}
			return list(merge);
		});

		if (removed.isEmpty()) {
			response.addBulkStr(null);
		}
		else {
			response.addBulkStr(removed.remove(0));
		}
	}
}

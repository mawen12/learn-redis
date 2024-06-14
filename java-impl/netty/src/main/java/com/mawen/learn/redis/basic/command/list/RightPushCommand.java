package com.mawen.learn.redis.basic.command.list;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.IRedisCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
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
@Command("rpush")
@ParamLength(2)
@ParamType(DataType.LIST)
public class RightPushCommand implements IRedisCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<SafeString> values = request.getParams().stream().skip(1).collect(Collectors.toList());

		DatabaseValue value = db.merge(safeKey(request.getParam(0)), list(values), (oldValue, newValue) -> {
			List<SafeString> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			merge.addAll(newValue.getValue());
			return list(merge);
		});

		response.addInt(value.<List<SafeString>>getValue().size());
	}
}

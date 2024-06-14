package com.mawen.learn.redis.basic.command.hash;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.IRedisCommand;
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
 * @since 2024/6/7
 */
@Command("hdel")
@ParamLength(2)
@ParamType(DataType.HASH)
public class HashDeleteCommand implements IRedisCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<SafeString> keys = request.getParams().stream().skip(1).collect(Collectors.toList());

		List<SafeString> removedKeys = new LinkedList<>();
		db.merge(safeKey(request.getParam(0)), EMPTY_HASH, (oldValue, newValue) -> {
			Map<SafeString, SafeString> merged = new HashMap<>();
			merged.putAll(oldValue.getValue());
			for (SafeString key : keys) {
				SafeString data = merged.remove(key);
				if (data != null) {
					removedKeys.add(data);
				}
			}
			return hash(merged.entrySet());
		});

		response.addInt(!removedKeys.isEmpty());
	}
}

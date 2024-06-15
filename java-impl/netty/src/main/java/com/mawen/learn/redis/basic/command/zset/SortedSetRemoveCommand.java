package com.mawen.learn.redis.basic.command.zset;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.data.SortedSet;
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
@Command("zrem")
@ParamLength(2)
@ParamType(DataType.ZSET)
public class SortedSetRemoveCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<SafeString> items = request.getParams().stream().skip(1).collect(Collectors.toList());
		List<SafeString> removed = new LinkedList<>();

		db.merge(safeKey(request.getParam(0)), EMPTY_ZSET, (oldValue, newValue) -> {
			Set<Map.Entry<Double, SafeString>> merge = new SortedSet();
			merge.addAll(oldValue.getValue());
			for (SafeString item : items) {
				if (merge.remove(item)) {
					removed.add(item);
				}
			}

			return zset(merge);
		});

		response.addInt(removed.size());
	}
}

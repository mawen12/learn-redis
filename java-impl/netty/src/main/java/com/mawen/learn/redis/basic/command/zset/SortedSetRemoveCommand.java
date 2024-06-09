package com.mawen.learn.redis.basic.command.zset;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.data.SortedSet;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("zrem")
@ParamLength(2)
@ParamType(DataType.ZSET)
public class SortedSetRemoveCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<String> items = request.getParams().stream().skip(1).collect(Collectors.toList());
		List<String> removed = new LinkedList<>();

		db.merge(request.getParam(0), zset(), (oldValue, newValue) -> {
			Set<Map.Entry<Float, String>> merge = new SortedSet();
			merge.addAll(oldValue.getValue());
			for (String item : items) {
				if (merge.remove(item)) {
					removed.add(item);
				}
			}

			return zset(merge);
		});

		response.addInt(removed.size());
	}
}

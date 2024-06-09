package com.mawen.learn.redis.basic.command.set;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("srem")
@ParamLength(2)
@ParamType(DataType.SET)
public class SetRemoveCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<String> items = request.getParams().stream().skip(1).collect(toList());
		List<String> removed = new LinkedList<>();
		db.merge(request.getParam(0), set(), (oldValue, newValue) -> {
			Set<String> merge = new HashSet<>();
			merge.addAll(oldValue.getValue());
			for (String item : items) {
				if (merge.remove(item)) {
					removed.add(item);
				}
			}
			return set(merge);
		});

		response.addInt(removed.size());
	}
}

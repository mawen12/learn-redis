package com.mawen.learn.redis.basic.command.zset;

import java.util.Map.Entry;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.data.SortedSet;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.lang.Float.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("zadd")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetAddCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue initial = db.getOrDefault(request.getParam(0), zset());
			DatabaseValue result = db.merge(request.getParam(0), parseInput(request), (oldValue, newValue) -> {
				Set<Entry<Float, String>> merge = new SortedSet();
				merge.addAll(oldValue.getValue());
				merge.addAll(newValue.getValue());
				return zset(merge);
			});

			response.addInt(changed(initial.getValue(), result.getValue()));
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not a valid float");
		}
	}

	private DatabaseValue parseInput(IRequest request) {
		Set<Entry<Float, String>> set = new SortedSet();
		String score = null;
		for (String string : request.getParams().stream().skip(1).collect(toList())) {
			if (score != null) {
				set.add(score(parseFloat(score), string));
				score = null;
			}
			else {
				score = string;
			}
		}

		return zset(set);
	}

	private int changed(Set<Entry<Float, String>> input, Set<Entry<Float, String>> result) {
		return result.size() - input.size();
	}
}

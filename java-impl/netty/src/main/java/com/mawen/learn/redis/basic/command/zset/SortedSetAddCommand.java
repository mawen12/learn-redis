package com.mawen.learn.redis.basic.command.zset;

import java.util.HashSet;
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
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

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
			DatabaseValue initial = db.getOrDefault(request.getParam(0), zset()).copy();
			DatabaseValue result = db.merge(request.getParam(0), parseInput(request), (oldValue, newValue) -> {
				Set<Map.Entry<Float, String>> oldSet = oldValue.getValue();
				Set<Map.Entry<Float, String>> newSet = newValue.getValue();
				oldSet.addAll(newSet);
				return oldValue;
			});
			response.addInt(changed(initial.getValue(), result.getValue()));
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not a valid float");
		}
	}

	private DatabaseValue parseInput(IRequest request) {
		Set<Map.Entry<Float, String>> set = new HashSet<>();
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

	private int changed(Set<Map.Entry<Float, String>> input, Set<Map.Entry<Float, String>> result) {
		return result.size() - input.size();
	}
}

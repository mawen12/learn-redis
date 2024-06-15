package com.mawen.learn.redis.basic.command.zset;

import java.util.Map.Entry;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.data.SortedSet;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.lang.Double.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
@Command("zadd")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetAddCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue initial = db.getOrDefault(safeKey(request.getParam(0)), EMPTY_ZSET);
			DatabaseValue result = db.merge(safeKey(request.getParam(0)), parseInput(request), (oldValue, newValue) -> {
				Set<Entry<Double, SafeString>> merge = new SortedSet();
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
		Set<Entry<Double, SafeString>> set = new SortedSet();
		SafeString score = null;
		for (SafeString string : request.getParams().stream().skip(1).collect(toList())) {
			if (score != null) {
				set.add(score(parseDouble(score.toString()), string));
				score = null;
			}
			else {
				score = string;
			}
		}

		return zset(set);
	}

	private int changed(Set<Entry<Float, SafeString>> input, Set<Entry<Float, SafeString>> result) {
		return result.size() - input.size();
	}
}

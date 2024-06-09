package com.mawen.learn.redis.basic.command.zset;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.stream.Stream;

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
import static java.lang.String.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@Command("zrange")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetRangeCommand implements ICommand {

	private static final String PARAM_WITHSCORES = "WITHSCORES";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.getOrDefault(request.getParam(0), zset());
			NavigableSet<Entry<Float, String>> set = value.getValue();

			int from = Integer.parseInt(request.getParam(1));
			if (from < 0) {
				from = set.size() + from;
			}
			int to = Integer.parseInt(request.getParam(2));
			if (to < 0) {
				to = set.size() + to;
			}

			Entry<?, ?>[] array = set.toArray(new Entry<?, ?>[] {});

			List<String> result = Collections.emptyList();
			if (from <= to) {
				Optional<String> withScores = request.getOptionalParam(3);
				if (withScores.isPresent() && withScores.get().equalsIgnoreCase(PARAM_WITHSCORES)) {
					result = Stream.of(array).skip(from).limit((to - from) + 1).flatMap(o -> Stream.of(valueOf(o.getValue()), valueOf(o.getKey()))).collect(toList());
				}
				else {
					result = Stream.of(array).skip(from).limit((to - from) + 1).map(o -> valueOf(o.getValue())).collect(toList());
				}
			}

			response.addArray(result);
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}
}

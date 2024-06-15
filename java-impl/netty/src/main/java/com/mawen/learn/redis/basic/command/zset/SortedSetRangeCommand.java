package com.mawen.learn.redis.basic.command.zset;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.stream.Stream;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
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
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@ReadOnly
@Command("zrange")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetRangeCommand implements ITinyDBCommand {

	private static final String PARAM_WITHSCORES = "WITHSCORES";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.getOrDefault(safeKey(request.getParam(0)), EMPTY_ZSET);
			NavigableSet<Entry<Float, SafeString>> set = value.getValue();

			int from = Integer.parseInt(request.getParam(1).toString());
			if (from < 0) {
				from = set.size() + from;
			}
			int to = Integer.parseInt(request.getParam(2).toString());
			if (to < 0) {
				to = set.size() + to;
			}

			List<Object> result = Collections.emptyList();
			if (from <= to) {
				Optional<SafeString> withScores = request.getOptionalParam(3);
				if (withScores.isPresent() && withScores.get().toString().equalsIgnoreCase(PARAM_WITHSCORES)) {
					result = set.stream().skip(from).limit((to - from) + 1).flatMap(o -> Stream.of(o.getValue(), o.getKey())).collect(toList());
				}
				else {
					result = set.stream().skip(from).limit((to - from) + 1).map(Entry::getValue).collect(toList());
				}
			}

			response.addArray(result);
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
	}
}

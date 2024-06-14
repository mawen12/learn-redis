package com.mawen.learn.redis.basic.command.zset;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.stream.Stream;

import com.mawen.learn.redis.basic.command.IRedisCommand;
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
import static java.lang.Integer.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@ReadOnly
@Command("zrangebyscore")
@ParamLength(3)
@ParamType(DataType.ZSET)
public class SortedSetRangeByScoreCommand implements IRedisCommand {

	private static final String EXCLUSIVE = "(";
	private static final String MINUS_INFINITY = "-inf";
	private static final String INFINITY = "+inf";
	private static final String PARAM_WITHSCORES = "WITHSCORES";
	private static final String PARAM_LIMIT = "LIMIT";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.getOrDefault(safeKey(request.getParam(0)), EMPTY_ZSET);
			NavigableSet<Map.Entry<Double, SafeString>> set = value.getValue();

			float from = parseRange(request.getParam(1).toString());
			float to = parseRange(request.getParam(2).toString());

			Options options = parseOptions(request);

			Set<Map.Entry<Double, SafeString>> range = set.subSet(
					score(from, SafeString.EMPTY_STRING), inclusive(request.getParam(1)),
					score(to, SafeString.EMPTY_STRING), inclusive(request.getParam(2)));

			List<Object> result = Collections.emptyList();
			if (from <= to) {
				if (options.withScores) {
					result = range.stream().flatMap(o -> Stream.of(o.getValue(), o.getKey())).collect(toList());
				}
				else {
					result = range.stream().map(Map.Entry::getValue).collect(toList());
				}

				if (options.withLimit) {
					result = result.stream().skip(options.offset).limit(options.count).collect(toList()); ;
				}
			}
			response.addArray(result);
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an float or out of rang");
		}
	}

	private Options parseOptions(IRequest request) {
		Options options = new Options();
		for (int i = 3; i < request.getLength(); i++) {
			String param = request.getParam(i).toString();
			if (param.equalsIgnoreCase(PARAM_LIMIT)) {
				options.withLimit = true;
				options.offset = parseInt(request.getParam(++i).toString());
				options.count = parseInt(request.getParam(++i).toString());
			}
			else if (param.equalsIgnoreCase(PARAM_WITHSCORES)) {
				options.withScores = true;
			}
		}
		return options;
	}

	private boolean inclusive(SafeString param) {
		return !param.toString().startsWith(EXCLUSIVE);
	}

	private float parseRange(String param) throws NumberFormatException {
		switch (param) {
			case INFINITY:
				return Float.MAX_VALUE;
			case MINUS_INFINITY:
				return Float.MIN_VALUE;
			default:
				if (param.startsWith(EXCLUSIVE)) {
					return Float.parseFloat(param.substring(1));
				}
				return Float.parseFloat(param);
		}
	}

	private static class Options {
		boolean withScores;
		boolean withLimit;
		int offset;
		int count;
	}
}

package com.mawen.learn.redis.basic.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class RedisResponse {

	private final IResponse response;

	public RedisResponse(IResponse response) {
		this.response = response;
	}

	public RedisResponse addValue(DatabaseValue value) {
		if (value != null) {
			switch (value.getType()) {
				case STRING:
					response.addBulkStr(value.getValue());
					break;
				case HASH:
					Map<SafeString, SafeString> map = value.getValue();
					response.addArray(keyValueList(map));
					break;
				case LIST:
				case SET:
				case ZSET:
					response.addArray(value.getValue());
					break;
				default:
					break;
			}
		}
		else {
			response.addBulkStr(null);
		}
		return this;
	}

	private List<SafeString> keyValueList(Map<SafeString, SafeString> map) {
		return map.entrySet().stream().flatMap(entry -> Stream.of(entry.getKey(), entry.getValue())).collect(toList());
	}

	public RedisResponse addArrayValue(Collection<DatabaseValue> array) {
		if (array != null) {
			response.addArray(array.stream().map(DatabaseValue::getValue).collect(toList()));
		}
		else {
			response.addArray(null);
		}
		return this;
	}
}

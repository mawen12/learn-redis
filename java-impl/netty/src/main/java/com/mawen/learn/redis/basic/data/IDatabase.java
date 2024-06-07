package com.mawen.learn.redis.basic.data;

import java.util.Map;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IDatabase extends Map<String, DatabaseValue> {

	boolean rename(String from, String to);

	boolean isType(String key, DataType type);
}

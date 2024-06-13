package com.mawen.learn.redis.basic.data;

import java.util.Map;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IDatabase extends Map<DatabaseKey, DatabaseValue> {

	boolean rename(DatabaseKey from, DatabaseKey to);

	boolean isType(DatabaseKey key, DataType type);
}

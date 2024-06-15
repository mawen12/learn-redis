package com.mawen.learn.redis.basic;

import java.util.Map;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class DatabaseValueMatchers {

	public static DatabaseValue list(String... strings) {
		return DatabaseValue.list(safeAsList(strings));
	}

	public static DatabaseValue set(String... strings) {
		return DatabaseValue.set(safeAsList(strings));
	}

	public static Map.Entry<SafeString, SafeString> entry(String key, String value) {
		return DatabaseValue.entry(safeString(key), safeString(value));
	}

	public static Map.Entry<Double, SafeString> score(double score, String value) {
		return DatabaseValue.score(score, safeString(value));
	}

	public static Matcher<DatabaseValue> isString(String expected) {
		return IsEqual.equalTo(string(expected));
	}

	public static Matcher<DatabaseValue> isList(String... expected) {
		return IsEqual.equalTo(list(expected));
	}

	public static Matcher<DatabaseValue> isSet(String... expected) {
		return IsEqual.equalTo(set(expected));
	}

	public static Matcher<DatabaseValue> notNullValue() {
		return IsNull.notNullValue(DatabaseValue.class);
	}

	public static Matcher<DatabaseValue> nullValue() {
		return IsNull.nullValue(DatabaseValue.class);
	}
}

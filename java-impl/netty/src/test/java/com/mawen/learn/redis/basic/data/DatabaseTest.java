package com.mawen.learn.redis.basic.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DatabaseTest {

	private final Database database = new Database();

	@Test
	public void testDatabase() {
		DatabaseValue value = string("value");

		database.put(safeKey("a"), value);

		assertThat(database.get(safeKey("a")).getValue(), is(safeString("value")));
		assertThat(database.containsKey(safeKey("a")), is(true));
		assertThat(database.containsKey(safeKey("b")), is(false));
		assertThat(database.isEmpty(), is(false));
		assertThat(database.size(), is(1));

		Collection<DatabaseValue> values = database.values();

		assertThat(values.size(), is(1));
		assertThat(values.contains(string("value")), is(true));

		Set<DatabaseKey> keySet = database.keySet();

		assertThat(keySet.size(), is(1));
		assertThat(keySet.contains(safeKey("a")), is(true));

		Set<Map.Entry<DatabaseKey, DatabaseValue>> entrySet = database.entrySet();

		assertThat(entrySet.size(), is(1));

		Map.Entry<DatabaseKey, DatabaseValue> entry = entrySet.iterator().next();

		assertThat(entry.getKey(), is(safeKey("a")));
		assertThat(entry.getValue(), is(string("value")));
	}

	@Test
	public void testExecute() {
		database.put(safeKey("a"), string("1"));
		database.overrideKey(safeKey("a", 10));
		database.getKey(safeKey(""));

		DatabaseKey key = database.getKey(safeKey("a"));
		assertThat(key, is(notNullValue()));
		assertThat(key.expiredAt(), is(greaterThan(0L)));
	}
}
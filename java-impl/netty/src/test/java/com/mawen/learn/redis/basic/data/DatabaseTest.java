package com.mawen.learn.redis.basic.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DatabaseTest {

	private final Database database = new Database();

	@Test
	public void testDatabase() {
		DatabaseValue value = string("value");

		database.put("a", value);

		assertThat(database.get("a").getValue(), is(safeString("value")));
		assertThat(database.containsKey("a"), is(true));
		assertThat(database.containsKey("b"), is(false));
		assertThat(database.isEmpty(), is(false));
		assertThat(database.size(), is(1));

		Collection<DatabaseValue> values = database.values();

		assertThat(values.size(), is(1));
		assertThat(values.contains(string("value")), is(true));

		Set<String> keySet = database.keySet();

		assertThat(keySet.size(), is(1));
		assertThat(keySet.contains("a"), is(true));

		Set<Map.Entry<String, DatabaseValue>> entrySet = database.entrySet();

		assertThat(entrySet.size(), is(1));

		Map.Entry<String, DatabaseValue> entry = entrySet.iterator().next();

		assertThat(entry.getKey(), is("a"));
		assertThat(entry.getValue(), is("value"));
	}

}
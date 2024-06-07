package com.mawen.learn.redis.basic.data;

import java.util.HashMap;

import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DatabaseTest {

	private Database database = new Database(new HashMap<>());

	@Test
	public void testDatabase() {
		DatabaseValue value = string("value");

		database.put("a", value);

		assertThat(database.get("a").getValue(), is("value"));
		assertThat(database.containsKey("a"), is(true));
		assertThat(database.containsKey("b"), is(false));
		assertThat(database.isEmpty(), is(false));
		assertThat(database.size(), is(1));
	}

}
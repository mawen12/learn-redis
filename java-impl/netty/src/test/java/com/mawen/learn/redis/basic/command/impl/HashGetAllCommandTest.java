package com.mawen.learn.redis.basic.command.impl;

import java.util.Map;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(HashGetAllCommand.class)
public class HashGetAllCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		rule.withData("a",hash(entry("key1", "value1"), entry("key2", "value2"), entry("key3", "value3")))
				.withParams("a")
				.execute()
				.verify().addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		Map<String, String> map = value.getValue();

		assertThat(map.get("key1"), is("value1"));
		assertThat(map.get("key2"), is("value2"));
		assertThat(map.get("key3"), is("value3"));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("a")
				.execute()
				.verify().addArray(anyCollectionOf(String.class));
	}

}
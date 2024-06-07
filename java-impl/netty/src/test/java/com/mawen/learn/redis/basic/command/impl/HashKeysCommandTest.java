package com.mawen.learn.redis.basic.command.impl;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HashKeysCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.getDatabase().put("key", hash(entry("a", "1"), entry("b", "2")));

		rule.withParams("key", "a").execute(new HashKeysCommand());

		verify(rule.getResponse()).addArray(captor.capture());

		Collection<String> keys = captor.getValue();

		assertThat(keys.size(), is(2));
		assertThat(keys.contains("a"), is(true));
		assertThat(keys.contains("b"), is(true));
	}

}
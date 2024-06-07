package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class HashExistsCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.getDatabase().put("key", hash(entry("a", "1")));

		rule.withParams("key", "a").execute(new HashExistsCommand());

		verify(rule.getResponse()).addInt(true);
	}
}
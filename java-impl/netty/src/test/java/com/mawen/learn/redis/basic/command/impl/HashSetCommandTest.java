package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class HashSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.getDatabase().put("a", hash(entry("key", "value")));

		rule.withParams("a", "key", "value").execute(new HashSetCommand());

		verify(rule.getResponse()).addInt(false);
	}
}
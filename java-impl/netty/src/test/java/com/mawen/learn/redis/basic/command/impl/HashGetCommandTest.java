package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class HashGetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.getDatabase().put("a", hash(entry("key", "value")));

		rule.withParams("a", "key").execute(new HashGetCommand());

		verify(rule.getResponse()).addBulkStr("value");
	}
}
package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class ExistsCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.getDatabase().put("test", string("value"));

		rule.withParams("test").execute(new ExistsCommand());

		verify(rule.getResponse()).addInt(true);
	}

}
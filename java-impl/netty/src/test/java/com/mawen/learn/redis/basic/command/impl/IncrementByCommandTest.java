package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IncrementByCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("a", "10").execute(new IncrementByCommand());

		verify(rule.getResponse()).addInt("10");

		rule.execute(new IncrementByCommand());

		verify(rule.getResponse()).addInt("20");
	}

}
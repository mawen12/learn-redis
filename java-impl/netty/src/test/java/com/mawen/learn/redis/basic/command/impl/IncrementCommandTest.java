package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IncrementCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a").execute(new IncrementCommand());

		verify(rule.getResponse()).addInt("1");

		rule.withParams("a").execute(new IncrementCommand());

		verify(rule.getResponse()).addInt("2");
	}
}
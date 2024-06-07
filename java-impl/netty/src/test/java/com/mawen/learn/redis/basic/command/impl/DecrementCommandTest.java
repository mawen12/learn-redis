package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DecrementCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a").execute(new DecrementCommand());

		verify(rule.getResponse()).addInt("-1");

		rule.execute(new DecrementCommand());

		verify(rule.getResponse()).addInt("-2");
	}
}
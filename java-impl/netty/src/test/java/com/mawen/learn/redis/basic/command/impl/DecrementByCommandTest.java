package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DecrementByCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a", "10").execute(new DecrementByCommand());

		verify(rule.getResponse()).addInt("-10");

		rule.execute(new DecrementByCommand());

		verify(rule.getResponse()).addInt("-20");
	}

}
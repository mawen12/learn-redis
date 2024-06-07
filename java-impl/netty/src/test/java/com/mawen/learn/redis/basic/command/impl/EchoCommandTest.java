package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EchoCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {

		rule.withParams("test").execute(new EchoCommand());

		verify(rule.getResponse()).addBulkStr("test");
	}
}
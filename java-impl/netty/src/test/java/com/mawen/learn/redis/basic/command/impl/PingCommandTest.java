package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PingCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {

		rule.execute(new PingCommand());

		verify(rule.getResponse()).addSimpleStr("PONG");
	}

	@Test
	public void testExecuteWithParam() {
		rule.withParams("HI!").execute(new PingCommand());

		verify(rule.getResponse()).addBulkStr("HI!");
	}
}
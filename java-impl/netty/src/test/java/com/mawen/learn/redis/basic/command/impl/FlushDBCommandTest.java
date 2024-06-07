package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlushDBCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {

		rule.execute(new FlushDBCommand());

		assertThat(rule.getDatabase().isEmpty(), is(true));

		verify(rule.getResponse()).addSimpleStr("OK");
	}
}
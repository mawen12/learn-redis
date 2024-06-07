package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class AppendCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.getDatabase().put("test", string("Hola"));

		rule.withParams("test", " mundo").execute(new AppendCommand());

		verify(rule.getResponse()).addInt(10);
	}

	@Test
	public void testExecuteNoExists() {

		rule.withParams("test", " mundo").execute(new AppendCommand());

		verify(rule.getResponse()).addInt(6);
	}

}
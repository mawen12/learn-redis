package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(AppendCommand.class)
public class AppendCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("test", string("Hola"))
				.withParams("test", " mundo")
				.execute()
				.verify().addInt(10);
	}

	@Test
	public void testExecuteNoExists() {
		rule.withParams("test", " mundo")
				.execute()
				.verify().addInt(6);
	}

}
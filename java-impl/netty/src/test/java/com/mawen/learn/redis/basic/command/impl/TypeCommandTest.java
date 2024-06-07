package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class TypeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.getDatabase().put("a", string("test"));

		rule.withParams("a").execute(new TypeCommand());

		verify(rule.getResponse()).addSimpleStr("string");
	}

}
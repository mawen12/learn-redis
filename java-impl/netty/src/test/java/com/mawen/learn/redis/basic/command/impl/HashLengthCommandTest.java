package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class HashLengthCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);


	@Test
	public void testExecute() {
		rule.getDatabase().put("key", hash(entry("a", "1"), entry("b", "2")));

		rule.withParams("key", "a").execute(new HashLengthCommand());

		verify(rule.getResponse()).addInt(2);
	}

}
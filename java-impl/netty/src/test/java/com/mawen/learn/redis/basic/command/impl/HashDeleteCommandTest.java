package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

public class HashDeleteCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.getDatabase().put("key", hash(entry("a", "1")));

		rule.withParams("key", "a", "b", "c").execute(new HashDeleteCommand());

		verify(rule.getResponse()).addInt(true);
	}
  
}
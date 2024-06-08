package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(HashLengthCommand.class)
public class HashLengthCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", hash(entry("a", "1"), entry("b", "2")))
				.withParams("key", "a")
				.execute()
				.verify().addInt(2);
	}

}
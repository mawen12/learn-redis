package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

@CommandUnderTest(IncrementByCommand.class)
public class IncrementByCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("a", "10")
				.execute()
				.verify().addInt("10");

		rule.withParams("a", "10")
				.execute()
				.verify().addInt("20");

		rule.withParams("a", "5")
				.execute()
				.verify().addInt("25");
	}

}
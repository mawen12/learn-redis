package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

@CommandUnderTest(IncrementCommand.class)
public class IncrementCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a")
				.execute()
				.verify().addInt("1");

		rule.withParams("a")
				.execute()
				.verify().addInt("2");

		rule.withParams("a")
				.execute()
				.verify().addInt("3");
	}
}
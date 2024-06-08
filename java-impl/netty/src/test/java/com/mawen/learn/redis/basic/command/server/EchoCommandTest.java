package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

@CommandUnderTest(EchoCommand.class)
public class EchoCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {

		rule.withParams("test")
				.execute()
				.verify().addBulkStr("test");
	}
}
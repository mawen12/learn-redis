package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.TinyDBRule;
import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

@CommandUnderTest(SlaveOfCommand.class)
public class SlaveOfCommandTest {

	@Rule
	public final TinyDBRule server = new TinyDBRule("localhost", 8081);

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("localhost", "8081")
				.execute()
				.verify().addSimpleStr("OK");
	}

}
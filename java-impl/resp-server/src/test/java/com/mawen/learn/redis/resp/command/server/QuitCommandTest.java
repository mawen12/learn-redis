package com.mawen.learn.redis.resp.command.server;

import com.mawen.learn.redis.resp.command.CommandRule;
import com.mawen.learn.redis.resp.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

@CommandUnderTest(QuitCommand.class)
public class QuitCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams()
				.execute()
				.verify().exit();
	}

}
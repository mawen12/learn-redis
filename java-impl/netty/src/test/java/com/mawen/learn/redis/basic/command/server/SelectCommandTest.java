package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.command.ISession;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

@CommandUnderTest(SelectCommand.class)
public class SelectCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("10")
				.execute()
				.verify(ISession.class).setCurrentDB(10);

		rule.withParams("asdasd")
				.execute()
				.verify().addError("ERR invalid DB index");
	}

}
package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.redis.SafeString.*;

@CommandUnderTest(PingCommand.class)
public class PingCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.execute()
				.verify().addSimpleStr("PONG");
	}

	@Test
	public void testExecuteWithParam() {
		rule.withParams("HI!")
				.execute()
				.verify().addBulkStr(safeString("HI!"));
	}
}
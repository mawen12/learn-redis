package com.mawen.learn.redis.resp.command.server;

import com.mawen.learn.redis.resp.command.CommandRule;
import com.mawen.learn.redis.resp.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;

@CommandUnderTest(PingCommand.class)
public class PingCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
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
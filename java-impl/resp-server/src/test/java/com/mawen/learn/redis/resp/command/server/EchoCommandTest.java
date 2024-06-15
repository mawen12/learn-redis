package com.mawen.learn.redis.resp.command.server;

import com.mawen.learn.redis.resp.command.CommandRule;
import com.mawen.learn.redis.resp.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;

@CommandUnderTest(EchoCommand.class)
public class EchoCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("test")
				.execute()
				.verify().addBulkStr(safeString("test"));
	}

}
package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;


@CommandUnderTest(IncrementByCommand.class)
public class IncrementByCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("a", "10")
				.execute()
				.verify().addInt(safeString("10"));

		rule.withParams("a", "10")
				.execute()
				.verify().addInt(safeString("20"));

		rule.withParams("a", "5")
				.execute()
				.verify().addInt(safeString("25"));
	}

}
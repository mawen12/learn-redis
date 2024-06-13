package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(SetIsMemberCommand.class)
public class SetIsMemberCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", setFromString("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.verify().addInt(true);

		rule.withParams("key", "d")
				.execute()
				.verify().addInt(false);
	}

}
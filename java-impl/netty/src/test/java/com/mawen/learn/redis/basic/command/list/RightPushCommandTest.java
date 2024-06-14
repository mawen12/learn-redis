package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;

@CommandUnderTest(RightPushCommand.class)
public class RightPushCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("key", "a", "b", "c")
				.execute()
				.assertValue("key", isList("a", "b", "c"))
				.verify().addInt(3);

		rule.withParams("key", "d")
				.execute()
				.assertValue("key", isList("a", "b", "c", "d"))
				.verify().addInt(4);
	}

}
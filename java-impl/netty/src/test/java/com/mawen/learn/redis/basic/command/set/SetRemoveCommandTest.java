package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;

@CommandUnderTest(SetRemoveCommand.class)
public class SetRemoveCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.assertValue("key", isSet("b", "c"))
				.verify().addInt(1);

		rule.withParams("key", "a")
				.execute()
				.assertValue("key", isSet("b", "c"))
				.verify().addInt(0);
	}

}
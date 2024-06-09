package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(SetCardinalityCommand.class)
public class SetCardinalityCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addInt(3);

		rule.withParams("notExists")
				.execute()
				.verify().addInt(0);
	}

}
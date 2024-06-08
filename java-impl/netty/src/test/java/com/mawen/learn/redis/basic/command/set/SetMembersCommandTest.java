package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(SetMembersCommand.class)
public class SetMembersCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addValue(set("a", "b", "c"));
	}

}
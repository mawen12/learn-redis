package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;


@CommandUnderTest(ListLengthCommand.class)
public class ListLengthCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", list("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addInt(3);
	}

}
package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(ExistsCommand.class)
public class ExistsCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("test",string("value"))
				.withParams("test")
				.execute()
				.verify().addInt(true);
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("test")
				.execute()
				.verify().addInt(false);
	}

}
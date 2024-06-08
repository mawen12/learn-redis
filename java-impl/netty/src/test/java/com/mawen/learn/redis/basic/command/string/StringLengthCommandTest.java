package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(StringLengthCommand.class)
public class StringLengthCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a",string("test"))
				.withParams("a")
				.execute()
				.verify().addInt(4);
	}
}
package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
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

}
package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;

@CommandUnderTest(SetAddCommand.class)
public class SetAddCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("key", "value")
				.execute()
				.assertValue("key", isSet("value"))
				.verify().addInt(1);
	}

}
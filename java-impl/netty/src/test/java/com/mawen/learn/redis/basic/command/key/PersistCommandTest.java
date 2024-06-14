package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(PersistCommand.class)
public class PersistCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("test", string("value"))
				.withParams("test")
				.execute()
				.verify().addInt(true);

		rule.withParams("notExists", "10")
				.execute()
				.verify().addInt(false);
	}

}
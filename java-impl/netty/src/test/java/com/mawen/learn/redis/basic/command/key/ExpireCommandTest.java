package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;

@CommandUnderTest(ExpireCommand.class)
public class ExpireCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("test", DatabaseValue.string("value"))
				.withParams("test", "10")
				.execute()
				.assertKey("test", isNotExpired())
				.assertValue("test", isString("value"))
				.verify().addInt(true);

		rule.withParams("notExists", "10")
				.execute()
				.verify().addInt(false);
	}

}
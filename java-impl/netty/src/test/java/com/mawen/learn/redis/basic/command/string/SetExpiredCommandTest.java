package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SetExpiredCommand.class)
public class SetExpiredCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws InterruptedException {
		rule.withParams("a", "1", "1")
				.execute()
				.assertValue("a", is(string("1")))
				.verify().addSimpleStr("OK");
		rule.getDatabase().get(safeKey("a"));

		Thread.sleep(500);
		rule.assertKey("a", isNotExpired())
				.assertValue("a", notNullValue());

		Thread.sleep(500);
		rule.assertKey("a", isExpired())
				.assertValue("a", nullValue());
	}
}
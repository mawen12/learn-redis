package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SetExpiredCommand.class)
public class SetExpiredCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a", "10", "1")
				.execute()
				.assertThat("a", is(string("1")))
				.verify().addSimpleStr("OK");
		rule.getDatabase().get(DatabaseKey.safeKey("a"));
	}
}
package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(RightPushCommand.class)
public class RightPushCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("key", "a", "b", "c")
				.execute()
				.assertThat("key", is(listFromString("a", "b", "c")))
				.verify().addInt(3);

		rule.withParams("key", "d")
				.execute()
				.assertThat("key", is(listFromString("a", "b", "c", "d")))
				.verify().addInt(4);
	}

}
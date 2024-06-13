package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SetRemoveCommand.class)
public class SetRemoveCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("key", setFromString("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.assertThat("key", is(setFromString("b", "c")))
				.verify().addInt(1);

		rule.withParams("key", "a")
				.execute()
				.assertThat("key", is(setFromString("b", "c")))
				.verify().addInt(0);
	}

}
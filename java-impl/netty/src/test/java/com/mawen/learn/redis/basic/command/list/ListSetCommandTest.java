package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(ListSetCommand.class)
public class ListSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "0", "A")
				.execute()
				.assertThat("key", is(list("A", "b", "c")))
				.verify().addSimpleStr("OK");

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "-1", "C")
				.execute()
				.assertThat("key", is(list("a", "b", "C")))
				.verify().addSimpleStr("OK");

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "z", "C")
				.execute()
				.assertThat("key", is(list("a", "b", "c")))
				.verify().addError(startsWith("ERR"));

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "99", "C")
				.execute()
				.assertThat("key", is(list("a", "b", "c")))
				.verify().addError(startsWith("ERR"));
	}

}
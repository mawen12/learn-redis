package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(MultiSetCommand.class)
public class MultiSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a", "1", "b", "2", "c", "3")
				.execute()
				.assertThat("a",is(string("1")))
				.assertThat("b",is(string("2")))
				.assertThat("c",is(string("3")))
				.verify().addSimpleStr("OK");

		assertThat(rule.getDatabase().size(), is(3));
	}

}
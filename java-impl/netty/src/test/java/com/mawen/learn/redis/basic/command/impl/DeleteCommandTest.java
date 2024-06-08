package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(DeleteCommand.class)
public class DeleteCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {

		rule.withData("test",string("value"))
				.withParams("test")
				.execute()
				.assertThat("test",is(nullValue()))
				.verify().addInt(1);
	}
}
package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(RenameCommand.class)
public class RenameCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a",string("1"))
				.withParams("a", "b")
				.execute()
				.assertThat("a",is(nullValue()))
				.assertThat("b",is(string("1")))
				.verify().addSimpleStr("OK");
	}

	@Test
	public void testExecuteError() {
		rule.withParams("a", "b")
				.execute()
				.assertThat("a",is(nullValue()))
				.assertThat("b",is(nullValue()))
				.verify().addError("ERR no such key");
	}

}
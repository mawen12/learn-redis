package com.mawen.learn.redis.basic.command.impl;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.command.ICommand.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RenameCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.getDatabase().put("a", string("1"));

		rule.withParams("a", "b").execute(new RenameCommand());

		assertThat(rule.getDatabase().get("a"), is(nullValue()));
		assertThat(rule.getDatabase().get("b"), is(string("1")));

		verify(rule.getResponse()).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testExecuteError() {
		rule.withParams("a", "b").execute(new RenameCommand());

		assertThat(rule.getDatabase().get("a"), is(nullValue()));
		assertThat(rule.getDatabase().get("b"), is(nullValue()));

		verify(rule.getResponse()).addError("ERR no such key");
	}

}
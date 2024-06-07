package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MultiSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a", "1", "b", "2", "c", "3").execute(new MultiSetCommand());

		assertThat(rule.getDatabase().get("a"), is(string("1")));
		assertThat(rule.getDatabase().get("b"), is(string("2")));
		assertThat(rule.getDatabase().get("c"), is(string("3")));
		assertThat(rule.getDatabase().size(), is(3));

		verify(rule.getResponse()).addSimpleStr("OK");
	}

}
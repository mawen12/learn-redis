package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.getDatabase().put("test", string("value"));

		when(rule.getRequest().getParams()).thenReturn(Arrays.asList("test"));

		rule.withParams("test").execute(new DeleteCommand());

		assertThat(rule.getDatabase().containsKey("test"), is(false));

		verify(rule.getResponse()).addInt(1);
	}
}
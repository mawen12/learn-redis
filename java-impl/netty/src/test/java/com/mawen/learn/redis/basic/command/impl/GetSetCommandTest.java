package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GetSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		rule.getDatabase().put("a", string("1"));

		rule.withParams("a", "2").execute(new GetSetCommand());

		verify(rule.getResponse()).addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		assertThat(value.getValue(), is("1"));
	}

}
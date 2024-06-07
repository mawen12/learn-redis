package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		rule.getDatabase().put("key", string("value"));

		rule.withParams("key").execute(new GetCommand());

		verify(rule.getResponse()).addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		assertThat(value.getType(), is(DataType.STRING));
		assertThat(value.getValue(), is("value"));
	}

}
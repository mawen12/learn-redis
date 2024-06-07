package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MultiGetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<DatabaseValue>> captor;

	@Test
	public void testExecute() {
		rule.getDatabase().put("a", string("1"));
		rule.getDatabase().put("c", string("2"));

		rule.withParams("a", "b", "c").execute(new MultiGetCommand());

		verify(rule.getResponse()).addArrayValue(captor.capture());

		Collection<DatabaseValue> result = captor.getValue();

		Iterator<DatabaseValue> iterator = result.iterator();
		DatabaseValue a = iterator.next();
		DatabaseValue b = iterator.next();
		DatabaseValue c = iterator.next();

		assertThat(a.getValue(), is("1"));
		assertThat(b, is(nullValue()));
		assertThat(c.getValue(), is("2"));
	}

}
package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MultiGetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Captor
	private ArgumentCaptor<Collection<DatabaseValue>> captor;

	@Test
	public void testExecute() {
		when(request.getParams()).thenReturn(Arrays.asList("a", "b", "c"));
		when(db.get("a")).thenReturn(string("1"));
		when(db.get("c")).thenReturn(string("2"));

		MultiGetCommand command = new MultiGetCommand();

		command.execute(db, request, response);

		verify(response).addArrayValue(captor.capture());

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
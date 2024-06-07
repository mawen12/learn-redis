package com.mawen.learn.redis.basic.command.impl;

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
public class GetSetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		when(db.put(eq("a"), any())).thenReturn(string("1"));
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("2");

		GetSetCommand command = new GetSetCommand();

		command.execute(db, request, response);

		verify(response).addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		assertThat(value.getValue(), is("1"));
	}

}
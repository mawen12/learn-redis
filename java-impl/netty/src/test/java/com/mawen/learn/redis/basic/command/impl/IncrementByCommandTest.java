package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IncrementByCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() throws Exception {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("10");
		when(db.merge(eq("a"), any(), any())).thenReturn(string("10"));

		IncrementByCommand command = new IncrementByCommand();

		command.execute(db, request, response);

		verify(response).addInt("10");
	}

}
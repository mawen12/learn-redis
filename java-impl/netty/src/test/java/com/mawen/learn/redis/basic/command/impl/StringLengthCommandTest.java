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
public class StringLengthCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a");
		when(db.getOrDefault(eq("a"), any())).thenReturn(string("test"));

		StringLengthCommand command = new StringLengthCommand();

		command.execute(db, request, response);

		verify(response).addInt(4);
	}
}
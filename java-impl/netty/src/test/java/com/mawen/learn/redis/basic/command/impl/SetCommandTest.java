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
public class SetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("1");

		SetCommand command = new SetCommand();

		command.execute(db, request, response);

		verify(db).put(eq("a"), eq(string("1")));

		verify(response).addSimpleStr("OK");
	}
}
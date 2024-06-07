package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.command.ICommand.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RenameCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("b");
		when(db.rename("a", "b")).thenReturn(true);

		RenameCommand command = new RenameCommand();

		command.execute(db, request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testExecuteError() {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("b");
		when(db.rename("a", "b")).thenReturn(false);

		RenameCommand command = new RenameCommand();

		command.execute(db, request, response);

		verify(response).addError("ERR no such key");
	}

}
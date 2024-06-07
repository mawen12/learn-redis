package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() throws Exception {
		when(request.getParams()).thenReturn(Arrays.asList("test"));
		when(db.remove("test")).thenReturn(new DatabaseValue(DataType.STRING));

		DeleteCommand command = new DeleteCommand();

		command.execute(db, request, response);

		verify(db).remove("test");

		verify(response).addInt(1);
	}
}
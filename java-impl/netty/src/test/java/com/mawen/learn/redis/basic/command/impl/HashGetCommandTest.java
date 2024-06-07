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
public class HashGetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() throws Exception {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("key");
		when(db.get("a")).thenReturn(hash(entry("key", "value")));

		HashGetCommand command = new HashGetCommand();

		command.execute(db, request, response);

		verify(response).addBulkStr("value");
	}
}
package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;

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
public class MultiSetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParams()).thenReturn(Arrays.asList("a", "1", "b", "2", "c", "3"));

		MultiSetCommand command = new MultiSetCommand();

		command.execute(db, request, response);

		verify(db).merge(eq("a"), eq(string("1")), any());
		verify(db).merge(eq("b"), eq(string("2")), any());
		verify(db).merge(eq("c"), eq(string("3")), any());
		verifyNoMoreInteractions(db);

		verify(response).addSimpleStr("OK");
	}

}
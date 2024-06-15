package com.mawen.learn.redis.resp.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NullCommandTest {

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		NullCommand nullCommand = new NullCommand();

		when(request.getCommand()).thenReturn("notExists");

		nullCommand.execute(request, response);

		verify(response).addError("ERR unknown command 'notExists'");
	}

}
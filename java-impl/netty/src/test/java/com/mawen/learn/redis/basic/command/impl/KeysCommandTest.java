package com.mawen.learn.redis.basic.command.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeysCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a??");
		when(db.keySet()).thenReturn(new HashSet<>(Arrays.asList("abc", "acd", "c")));

		KeysCommand command = new KeysCommand();

		command.execute(db, request, response);

		verify(response).addArray(captor.capture());

		Collection<String> value = captor.getValue();

		assertThat(value.size(), is(2));
		assertThat(value.contains("abc"), is(true));
		assertThat(value.contains("acd"), is(true));
		assertThat(value.contains("c"), is(false));
	}

}
package com.mawen.learn.redis.basic.command.impl;

import java.util.Map;

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
public class HashGetAllCommandTest {

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
		when(request.getParam(0)).thenReturn("a");
		when(db.get("a")).thenReturn(hash(entry("key1", "value1"), entry("key2", "value2"), entry("key3", "value3")));

		HashGetAllCommand command = new HashGetAllCommand();

		command.execute(db, request, response);

		verify(response).addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		Map<String, String> map = value.getValue();

		assertThat(map.get("key1"), is("value1"));
		assertThat(map.get("key2"), is("value2"));
		assertThat(map.get("key3"), is("value3"));
	}

}
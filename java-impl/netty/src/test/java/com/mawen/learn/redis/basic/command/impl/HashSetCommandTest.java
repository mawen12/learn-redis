package com.mawen.learn.redis.basic.command.impl;

import java.util.HashMap;

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
public class HashSetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("key");
		when(request.getParam(2)).thenReturn("value");

		when(db.merge(eq("a"), any(), any())).thenReturn(new DatabaseValue(DataType.HASH, map()));

		HashSetCommand command = new HashSetCommand();

		command.execute(db, request, response);

		verify(response).addInt(false);
	}

	private HashMap<String, String> map() {
		HashMap<String, String> map = new HashMap<>();
		map.put("key", "value");
		return map;
	}
}
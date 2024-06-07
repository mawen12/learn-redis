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
		when(db.get("a")).thenReturn(new DatabaseValue(DataType.HASH, map()));

		HashGetCommand command = new HashGetCommand();

		command.execute(db, request, response);

		verify(response).addBulkStr("value");
	}

	private HashMap<String, String> map() {
		HashMap<String, String> map = new HashMap<>();
		map.put("key", "value");
		return map;
	}

}
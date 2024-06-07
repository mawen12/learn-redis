package com.mawen.learn.redis.basic.command.impl;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimeCommandTest {

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
		TimeCommand command = new TimeCommand();

		command.execute(db, request, response);

		verify(response).addArray(captor.capture());

		Collection<String> value = captor.getValue();

		Iterator<String> iterator = value.iterator();
		String secs = iterator.next();
		String mics = iterator.next();

		System.out.println(secs);
		System.out.println(mics);
	}
}
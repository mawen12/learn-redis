package com.mawen.learn.redis.basic.command.impl;

import java.util.function.BiFunction;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SetCommandTest {

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		when(request.getParam(0)).thenReturn("a");
		when(request.getParam(1)).thenReturn("1");

		SetCommand command = new SetCommand();

		command.execute(db, request, response);

		verify(db).merge(eq("a"),
				argThat(new BaseMatcher<DatabaseValue>() {
					@Override
					public boolean matches(Object value) {
						DatabaseValue db = (DatabaseValue) value;
						return db.getType() == DataType.STRING && db.getValue().equals("1");
					}

					@Override
					public void describeTo(Description description) {
						description.appendText("value expected: \"1\"");
					}
				}),
				argThat(new BaseMatcher<BiFunction<DatabaseValue, DatabaseValue, DatabaseValue>>() {
					@Override
					public boolean matches(Object value) {
						BiFunction<DatabaseValue, DatabaseValue, DatabaseValue> function = (BiFunction<DatabaseValue, DatabaseValue, DatabaseValue>) value;
						DatabaseValue db = function.apply(value("0"), value("1"));
						return db.getValue().equals("1");
					}

					@Override
					public void describeTo(Description description) {
						description.appendText("value expected: \"1\"");
					}
				}));

		verify(response).addSimpleStr("OK");
	}

	private DatabaseValue value(String value) {
		return new DatabaseValue(DataType.STRING, value);
	}
}
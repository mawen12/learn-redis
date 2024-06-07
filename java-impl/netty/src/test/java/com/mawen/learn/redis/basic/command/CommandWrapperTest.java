package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandWrapperTest {

	private static final String RESULT_OK = "OK";

	@Spy
	private final SomeCommand command = new SomeCommand();

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Test
	public void testExecute() {
		CommandWrapper wrapper = new CommandWrapper(command);

		wrapper.execute(db, request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testLengthOK() {
		when(request.getLength()).thenReturn(3);

		CommandWrapper wrapper = new CommandWrapper(new LengthCommand());

		wrapper.execute(db, request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testLengthKO() {
		when(request.getLength()).thenReturn(1);

		CommandWrapper wrapper = new CommandWrapper(new LengthCommand());

		wrapper.execute(db, request, response);

		verify(response, times(0)).addSimpleStr(RESULT_OK);

		verify(response).addError(anyString());
	}

	@Test
	public void testTypeOK() {
		when(db.isType(anyString(), eq(DataType.STRING))).thenReturn(true);
		when(request.getParam(0)).thenReturn("test");

		CommandWrapper wrapper = new CommandWrapper(new TypeCommand());

		wrapper.execute(db, request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testTypeKO() {
		when(db.isType(anyString(), eq(DataType.STRING))).thenReturn(false);
		when(request.getParam(0)).thenReturn("test");

		CommandWrapper wrapper = new CommandWrapper(new TypeCommand());

		wrapper.execute(db, request, response);

		verify(response, times(0)).addSimpleStr(RESULT_OK);

		verify(response).addError(anyString());
	}


	private class SomeCommand implements ICommand {

		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}

	@ParamLength(2)
	private class LengthCommand implements ICommand {
		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}

	@ParamType(DataType.STRING)
	private class TypeCommand implements ICommand {
		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}
}
package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDBServerState;
import com.mawen.learn.redis.basic.TinyDBSessionState;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.ISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.resp.command.IResponse.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandWrapperTest {

	@Spy
	private final SomeCommand command = new SomeCommand();

	@Mock
	private IDatabase db;

	@Mock
	private IRequest request;

	@Mock
	private IResponse response;

	@Mock
	private ISession session;

	@Mock
	private ITinyDB server;

	@Mock
	private TinyDBSessionState sessionState;

	@Mock
	private TinyDBServerState serverState;

	@Before
	public void setUp() {
		when(request.getSession()).thenReturn(session);
		when(request.getServerContext()).thenReturn(server);
		when(session.getValue("state")).thenReturn(sessionState);
		when(server.getValue("state")).thenReturn(serverState);
		when(sessionState.getCurrentDB()).thenReturn(1);
		when(serverState.getDatabase(1)).thenReturn(db);
	}

	@Test
	public void testExecute() {
		TinyDBCommandWrapper wrapper = new TinyDBCommandWrapper(new SomeCommand());

		wrapper.execute(request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testLengthOK() {
		when(request.getLength()).thenReturn(3);

		TinyDBCommandWrapper wrapper = new TinyDBCommandWrapper(new LengthCommand());

		wrapper.execute(request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testLengthKO() {
		when(request.getLength()).thenReturn(1);

		TinyDBCommandWrapper wrapper = new TinyDBCommandWrapper(new LengthCommand());

		wrapper.execute(request, response);

		verify(response, times(0)).addSimpleStr(RESULT_OK);

		verify(response).addError(anyString());
	}

	@Test
	public void testTypeOK() {
		when(db.isType(any(DatabaseKey.class), eq(DataType.STRING))).thenReturn(true);
		when(request.getParam(0)).thenReturn(safeString("test"));

		TinyDBCommandWrapper wrapper = new TinyDBCommandWrapper(new TypeCommand());

		wrapper.execute(request, response);

		verify(response).addSimpleStr(RESULT_OK);
	}

	@Test
	public void testTypeKO() {
		when(db.isType(any(DatabaseKey.class), eq(DataType.STRING))).thenReturn(false);
		when(request.getParam(0)).thenReturn(safeString("test"));

		TinyDBCommandWrapper wrapper = new TinyDBCommandWrapper(new TypeCommand());

		wrapper.execute(request, response);

		verify(response, times(0)).addSimpleStr(RESULT_OK);

		verify(response).addError(anyString());
	}


	private static class SomeCommand implements ITinyDBCommand {

		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}

	@ParamLength(2)
	private static class LengthCommand implements ITinyDBCommand {
		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}

	@ParamType(DataType.STRING)
	private static class TypeCommand implements ITinyDBCommand {
		@Override
		public void execute(IDatabase db, IRequest request, IResponse response) {
			response.addSimpleStr(RESULT_OK);
		}
	}
}
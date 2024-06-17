package com.mawen.learn.redis.basic.replication;

import java.io.IOException;
import java.io.InputStream;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDBRule;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.ISession;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SlaveReplicationTest {

	@Rule
	public final TinyDBRule rule = new TinyDBRule();

	@Mock
	private ITinyDB context;

	@Mock
	private ISession session;

	@Mock
	private ICommand command;

	@Captor
	private ArgumentCaptor<IRequest> requestCaptor;

	@Captor
	private ArgumentCaptor<InputStream> captor;

	@Test
	public void testReplication() throws IOException {
		SlaveReplication slave = new SlaveReplication(context, session, ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT);

		slave.start();

		verifyConnectionAndRDBDumpImported();
	}

	@Test
	public void testProcessCommand() {
		when(context.getCommand("PING")).thenReturn(command);

		SlaveReplication slave = new SlaveReplication(context, session, "localhost", 7081);

		slave.onMessage(new RedisToken.ArrayRedisToken(asList(new RedisToken.StringRedisToken(SafeString.safeString("PING")))));

		verifyCommandExecuted();
	}

	private void verifyCommandExecuted() {
		verify(command).execute(requestCaptor.capture(), any(IResponse.class));

		IRequest request = requestCaptor.getValue();
		assertThat(request.getCommand(), is("PING"));
	}

	private void verifyConnectionAndRDBDumpImported() throws IOException {
		verify(context, timeout(2000)).importRDB(captor.capture());

		InputStream stream = captor.getValue();

		byte[] buffer = new byte[stream.available()];

		int readed = stream.read(buffer);

		assertThat(readed, is(buffer.length));
		assertThat(toHexString(buffer), is("524544495330303036FF224AF218835A1E69"));
	}

}
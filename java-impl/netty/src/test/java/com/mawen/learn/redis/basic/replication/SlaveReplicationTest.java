package com.mawen.learn.redis.basic.replication;

import java.io.IOException;
import java.io.InputStream;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDB;
import com.mawen.learn.redis.basic.TinyDBRule;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.ISession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SlaveReplicationTest {

	@Rule
	public final TinyDBRule rule = new TinyDBRule();

	@Mock
	private IServerContext context;

	@Mock
	private ISession session;

	@Captor
	private ArgumentCaptor<InputStream> captor;

	@Test
	public void testReplication() throws IOException {
		SlaveReplication slave = new SlaveReplication(context, session, ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT);

		slave.start();

		verify(context, timeout(2000)).importRDB(captor.capture());

		InputStream stream = captor.getValue();

		byte[] buffer = new byte[stream.available()];

		int readed = stream.read(buffer);

		assertThat(readed, is(buffer.length));
		assertThat(toHexString(buffer), is("524544495330303036FF224AF218835A1E69"));
	}

}
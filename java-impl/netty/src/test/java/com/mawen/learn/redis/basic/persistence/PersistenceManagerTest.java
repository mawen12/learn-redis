package com.mawen.learn.redis.basic.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static com.mawen.learn.redis.basic.TinyDBConfig.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceManagerTest {

	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String REDO_FILE = "redo.aof";
	private static final String DUMP_FILE = "dump.rdb";

	@Mock
	private ITinyDB server;

	private PersistenceManager manager;

	@Before
	public void setUp() throws Exception {
		this.manager = new PersistenceManager(server, withPersistence());
		deleteFiles();
	}

	@After
	public void tearDown() throws Exception {
		deleteFiles();
	}

	private void deleteFiles() {
		deleteFile(DUMP_FILE);
		deleteFile(REDO_FILE);
	}

	private void deleteFile(String name) {
		File file = new File(name);
		if (file.exists()) {
			if (file.delete()) {
				System.out.println(file + " deleted");
			}
		}
	}

	@Test
	public void testRun() throws IOException {
		doAnswer(new ExportRDB()).when(server).exportRDB(any());

		manager.run();

		RDBInputStream input = new RDBInputStream(new FileInputStream(DUMP_FILE));

		Map<Integer, IDatabase> databases = input.parse();

		assertThat(databases, notNullValue());
	}

	@Test
	public void testStart() throws IOException {
		ICommand cmd = mock(ICommand.class);
		when(server.getCommand(anyString())).thenReturn(cmd);

		writeRDB();
		writeAOF();

		manager.start();

		verify(server).importRDB(any());
		verify(cmd).execute(any(), any());

		assertThat(new File(REDO_FILE).exists(), is(true));
	}

	@Test
	public void testAppend() throws InterruptedException {
		manager.start();
		manager.append(array());

		Thread.sleep(1000);

		assertThat(readAOF(), is("*1\r\n$4\r\nPING\r\n"));
	}

	private String readAOF() {
		String str = null;
		try (FileInputStream in = new FileInputStream(REDO_FILE)) {
			byte[] buffer = new byte[1024];
			int readed = in.read(buffer);
			if (readed > -1) {
				str = new String(buffer, 0, readed, DEFAULT_CHARSET);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	private List<RedisToken> array() {
		List<RedisToken> array = new LinkedList<>();
		array.add(token("PING"));
		return array;
	}

	private RedisToken token(String string) {
		return new RedisToken.StringRedisToken(safeString(string));
	}

	private void writeRDB() {
		try (FileOutputStream out = new FileOutputStream(DUMP_FILE)) {
			out.write("Test".getBytes(DEFAULT_CHARSET));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeAOF() throws IOException {
		try (FileOutputStream out = new FileOutputStream(DUMP_FILE)) {
			out.write("*1\r\n$4\r\nPING\r\n".getBytes(DEFAULT_CHARSET));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class ExportRDB implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			OutputStream output = (OutputStream) invocation.getArguments()[0];
			output.write(HexUtil.toByteArray("524544495330303033FE00FF77DE0394AC9D23EA"));
			return null;
		}
	}

}
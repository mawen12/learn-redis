package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.command.CommandSuite;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.RedisTokenType;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisServerTest {

	private static final String HOST = "localhost";
	private static final int PORT = 12345;
	private static final int TIMEOUT = 1000;

	private RedisServer redisServer;

	private final CommandSuite commands = new CommandSuite();

	private final IRedisCallback callback = mock(IRedisCallback.class);

	@Before
	public void setUp() throws Exception {
		redisServer = new RedisServer(HOST, PORT, commands);
		redisServer.start();
	}

	@After
	public void tearDown() throws Exception {
		redisServer.stop();
	}

	@Test
	public void serverRespond() {
		RedisClient client = createClient();

		client.send(RedisToken.array(RedisToken.string("PING\r\n")));

		verifyResponse("PONG");
	}

	@Test
	public void clientDisconnects() {
		RedisClient client = createClient();

		client.stop();

		verify(callback, timeout(TIMEOUT)).onDisconnect();
	}

	@Test
	public void serverDisconnects() {
		RedisClient client = createClient();

		redisServer.stop();

		verify(callback, timeout(TIMEOUT)).onDisconnect();

		client.stop();
	}

	@Test(expected = NullPointerException.class)
	public void requireHost() {
		new RedisServer(null, 0, commands);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requirePortLowerThan1024() {
		new RedisServer(HOST, 0, commands);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requirePortGreaterThan65535() {
		new RedisServer(HOST, 987654321, commands);
	}

	@Test(expected = NullPointerException.class)
	public void requireCallback() {
		new RedisServer(HOST, PORT, null);
	}

	private RedisClient createClient() {
		RedisClient redisClient = new RedisClient(HOST, PORT, callback);
		redisClient.start();
		verify(callback, timeout(TIMEOUT)).onConnect();
		return redisClient;
	}

	private void verifyResponse(String response) {
		ArgumentCaptor<RedisToken> captor = ArgumentCaptor.forClass(RedisToken.class);

		verify(callback, timeout(TIMEOUT)).onMessage(captor.capture());

		RedisToken token = captor.getValue();
		assertThat(token.getType(), equalTo(RedisTokenType.STATUS));
		assertThat(token.<SafeString>getValue(), equalTo(safeString(response)));
	}
}
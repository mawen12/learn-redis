package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.RedisTokenType;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.mawen.learn.redis.resp.protocol.RedisToken.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisClientTest {

	private static final String HOST = "localhost";

	private static final int PORT = 12345;

	private static final int TIMEOUT = 1000;

	@Rule
	public RedisServerRule redisServerRule = new RedisServerRule(HOST, PORT);

	private RedisClient redisClient;

	private final IRedisCallback callback = mock(IRedisCallback.class);

	@Before
	public void setUp() throws Exception {
		redisClient = new RedisClient(HOST, PORT, callback);
	}

	@Test
	public void onConnect() {
		redisClient.start();

		verify(callback, timeout(1000)).onConnect();
	}

	@Test
	public void onMessage() {
		redisClient.start();
		verify(callback, timeout(TIMEOUT)).onConnect();

		redisClient.send(array(string("PING\r\n")));

		ArgumentCaptor<RedisToken> captor = ArgumentCaptor.forClass(RedisToken.class);

		verify(callback, timeout(TIMEOUT)).onMessage(captor.capture());

		RedisToken token = captor.getValue();
		assertThat(token.getType(), equalTo(RedisTokenType.STATUS));
		assertThat(token.<SafeString>getValue(), equalTo(safeString("PONG")));
	}

	@Test
	public void onBigMessage() {
		redisClient.start();
		verify(callback, timeout(TIMEOUT)).onConnect();

		redisClient.send(array(string("PING"), string(readBigFile())));

		ArgumentCaptor<RedisToken> captor = ArgumentCaptor.forClass(RedisToken.class);

		verify(callback, timeout(TIMEOUT)).onMessage(captor.capture());

		RedisToken token = captor.getValue();
		assertThat(token.getType(), equalTo(RedisTokenType.STRING));
	}

	private String readBigFile() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			sb.append("lkjsdfkjaskjflskjf");
		}
		return sb.toString();
	}

	@Test
	public void onClientDisconnect() {
		redisClient.start();
		verify(callback, timeout(TIMEOUT)).onConnect();

		redisClient.stop();
		verify(callback, timeout(TIMEOUT)).onDisconnect();
	}

	@Test(expected = NullPointerException.class)
	public void requireHost() {
		new RedisClient(null, 0, callback);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requirePortLowerThan1024() {
		new RedisClient(HOST, 0, callback);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requirePortGreaterThan65535() {
		new RedisClient(HOST, 98765431, callback);
	}

	@Test(expected = NullPointerException.class)
	public void requireCallback() {
		new RedisClient("localhost", 12345, null);
	}
}
package com.mawen.learn.redis.basic;

import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.RedisTokenType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TinyDBClientTest {

	@Rule
	public final TinyDBRule rule = new TinyDBRule();

	@Test
	public void testClient() {
		ArgumentCaptor<RedisToken> captor = ArgumentCaptor.forClass(RedisToken.class);

		ITinyDBCallback callback = mock(ITinyDBCallback.class);
		TinyDBClient client = new TinyDBClient(callback);

		client.start();

		verify(callback, timeout(1000)).onConnect();

		client.send("ping\r\n");

		verify(callback, timeout(1000)).onMessage(captor.capture());

		RedisToken message = captor.getValue();

		assertThat(message.getType(), is(RedisTokenType.STATUS));
		assertThat(message.getValue(), is("PONG"));

		client.stop();

		verify(callback, timeout(1000)).onDisconnect();
	}

}
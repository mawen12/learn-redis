package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/17
 */
public class RedisParserTest {

	private final RedisSource source = mock(RedisSource.class);

	private final RedisParser parser = new RedisParser(100000, source);

	private final RedisToken intToken = RedisToken.integer(1);
	private final RedisToken abcString = RedisToken.string("abc");
	private final RedisToken pongString = RedisToken.status("pong");
	private final RedisToken errorString = RedisToken.error("ERR");
	private final RedisToken arrayToken = RedisToken.array(intToken, abcString);
	private final RedisToken unknownString = new RedisToken.UnknownRedisToken(safeString("what?"));

	@Test
	public void testBulkString() {
		when(source.readLine()).thenReturn(safeString("$3"));
		when(source.readString(3)).thenReturn(safeString("abc"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(abcString));
	}

	@Test
	public void testSimpleString() {
		when(source.readLine()).thenReturn(safeString("+pong"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(pongString));
	}

	@Test
	public void testInteger() {
		when(source.readLine()).thenReturn(safeString(":1"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(intToken));
	}

	@Test
	public void testErrorString() {
		when(source.readLine()).thenReturn(safeString("-ERR"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(errorString));
	}

	@Test
	public void testUnknownString() {
		when(source.readLine()).thenReturn(safeString("what?"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(unknownString));
	}

	@Test
	public void testArray() {
		when(source.readLine()).thenReturn(safeString("*2"), safeString(":1"), safeString("$3"));
		when(source.readString(3)).thenReturn(safeString("abc"));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(arrayToken));
	}

}

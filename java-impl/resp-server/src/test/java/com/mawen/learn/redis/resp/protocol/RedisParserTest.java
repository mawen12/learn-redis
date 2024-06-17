package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

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

	private final Charset utf8 = StandardCharsets.UTF_8;

	private final RedisToken intToken = RedisToken.integer(1);
	private final RedisToken abcString = RedisToken.string("abc");
	private final RedisToken pongString = RedisToken.status("pong");
	private final RedisToken errorString = RedisToken.error("ERR");
	private final RedisToken arrayToken = RedisToken.array(intToken, abcString);
	private final RedisToken unknownString = new RedisToken.UnknownRedisToken("what?");

	@Test
	public void testBulkString() {
		when(source.readLine()).thenReturn("$3");
		when(source.readBytes(3)).thenReturn(ByteBuffer.wrap("abc".getBytes(utf8)));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(abcString));
	}

	@Test
	public void testSimpleString() {
		when(source.readLine()).thenReturn("+pong");

		RedisToken token = parser.parse();

		assertThat(token, equalTo(pongString));
	}

	@Test
	public void testInteger() {
		when(source.readLine()).thenReturn(":1");

		RedisToken token = parser.parse();

		assertThat(token, equalTo(intToken));
	}

	@Test
	public void testErrorString() {
		when(source.readLine()).thenReturn("-ERR");

		RedisToken token = parser.parse();

		assertThat(token, equalTo(errorString));
	}

	@Test
	public void testUnknownString() {
		when(source.readLine()).thenReturn("what?");

		RedisToken token = parser.parse();

		assertThat(token, equalTo(unknownString));
	}

	@Test
	public void testArray() {
		when(source.readLine()).thenReturn("*2", ":1", "$3");
		when(source.readBytes(3)).thenReturn(ByteBuffer.wrap("abc".getBytes(utf8)));

		RedisToken token = parser.parse();

		assertThat(token, equalTo(arrayToken));
	}

}

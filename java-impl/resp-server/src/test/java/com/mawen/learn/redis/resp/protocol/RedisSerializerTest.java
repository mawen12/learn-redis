package com.mawen.learn.redis.resp.protocol;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RedisSerializerTest {

	private final Charset utf8 = StandardCharsets.UTF_8;

	private final RedisSerializer encoder = new RedisSerializer();

	private final RedisToken intToken = RedisToken.integer(1);
	private final RedisToken abcString = RedisToken.string("abc");
	private final RedisToken pongString = RedisToken.status("pong");
	private final RedisToken errorString = RedisToken.error("ERR");
	private final RedisToken arrayToken = RedisToken.array(intToken, abcString);

	@Test
	public void encodeString() throws Exception {
		assertThat(encoder.encodedToken(abcString), equalTo("$3\r\nabc\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeStatus() throws Exception {
		assertThat(encoder.encodedToken(pongString), equalTo("+pong\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeInteger() throws Exception {
		assertThat(encoder.encodedToken(intToken), equalTo(":1\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeError() throws Exception {
		assertThat(encoder.encodedToken(errorString), equalTo("-ERR\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeArray() throws Exception {
		assertThat(encoder.encodedToken(arrayToken), equalTo("*2\r\n:1\r\n$3\r\nabc\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeArrayOfArrays() throws Exception {
		assertThat(encoder.encodedToken(arrayToken), equalTo("*2\r\n*2\r\n:1\r\n$3\r\nabc\r\n*2\r\n:1\r\n$3\r\nabc\r\n".getBytes(utf8)));
	}
}
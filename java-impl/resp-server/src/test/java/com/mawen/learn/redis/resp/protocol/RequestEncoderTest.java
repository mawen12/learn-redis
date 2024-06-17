package com.mawen.learn.redis.resp.protocol;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestEncoderTest {

	private final Charset utf8 = StandardCharsets.UTF_8;

	private final RequestEncoder encoder = new RequestEncoder();

	private final RedisToken intToken = RedisToken.integer(1);
	private final RedisToken abcString = RedisToken.string("abc");
	private final RedisToken pongString = RedisToken.status("pong");
	private final RedisToken errorString = RedisToken.error("ERR");
	private final RedisToken arrayToken = RedisToken.array(intToken, abcString);

	private ByteBuf out = mock(ByteBuf.class);
	private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
	private ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);

	@Test
	public void encodeString() throws Exception {
		encoder.encode(ctx, abcString, out);

		verify(out).writeBytes(captor.capture());

		assertThat(captor.getValue(), equalTo("$3\r\nabc\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeStatus() throws Exception {
		encoder.encode(ctx, pongString, out);

		verify(out).writeBytes(captor.capture());

		assertThat(captor.getValue(), equalTo("+pong\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeInteger() throws Exception {
		encoder.encode(ctx, intToken, out);

		verify(out).writeBytes(captor.capture());

		assertThat(captor.getValue(), equalTo(":1\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeError() throws Exception {
		encoder.encode(ctx, errorString, out);

		verify(out).writeBytes(captor.capture());

		assertThat(captor.getValue(), equalTo("-ERR\r\n".getBytes(utf8)));
	}

	@Test
	public void encodeArray() throws Exception {
		encoder.encode(ctx, arrayToken, out);

		verify(out).writeBytes(captor.capture());

		assertThat(captor.getValue(), equalTo("*2\r\n:1\r\n$3\r\nabc\r\n".getBytes(utf8)));
	}


}
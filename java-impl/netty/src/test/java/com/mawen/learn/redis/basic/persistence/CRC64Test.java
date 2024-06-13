package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import static com.mawen.learn.redis.basic.persistence.ByteUtils.toByteArray;
import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CRC64Test {

	@Test
	public void testOne() throws IOException {
		CRC64 crc = new CRC64();
		byte[] bytes = "123456789".getBytes("UTF-8");
		for (byte b : bytes) {
			crc.update(b);
		}

		assertThat(toHexString(toByteArray(crc.getValue())), is("995DC9BBDF1939FA"));
	}

	@Test
	public void testString() throws UnsupportedEncodingException {
		CRC64 crc = new CRC64();
		byte[] bytes = "This is a test of the emergency broadcast system.".getBytes("UTF-8");
		crc.update(bytes, 0, bytes.length);

		assertThat(toHexString(toByteArray(crc.getValue())), is("27DB187FC15BBC72"));
	}

	@Test
	public void testTest() {
		CRC64 crc = new CRC64();
		byte[] bytes = HexUtil.toByteArray("524544495330303033FE00FF");
		crc.update(bytes, 0, bytes.length);

		assertThat(toHexString(toByteArray(crc.getValue())), is("77DE0394AC9D23EA"));
	}
}
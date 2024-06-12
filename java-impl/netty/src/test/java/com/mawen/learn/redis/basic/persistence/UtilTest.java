package com.mawen.learn.redis.basic.persistence;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UtilTest {

	@Test
	public void testInt() {
		byte[] array = Util.toByteArray(1234567890);

		System.out.println(HexUtil.toHexString(array));

		int i = Util.byteArrayToInt(array);

		assertThat(i, is(1234567890));
	}

	@Test
	public void testLong() {
		byte[] array = Util.toByteArray(1234567890987654321L);

		System.out.println(HexUtil.toHexString(array));

		long l = Util.byteArrayToLong(array);

		assertThat(l, is(1234567890987654321L));
	}

}
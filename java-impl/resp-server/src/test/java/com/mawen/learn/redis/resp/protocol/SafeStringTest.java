package com.mawen.learn.redis.resp.protocol;

import java.util.List;

import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SafeStringTest {

	@Test
	public void testBytes() {
		SafeString str = SafeString.safeString("Hola Mundo!");

		assertThat(new SafeString(str.getBuffer()), is(str));
		assertThat(str.length(), is(11));
		assertThat(HexUtil.toHexString(str.getBytes()), is("486F6C61204D756E646F21"));
		assertThat(str.toString(), is("Hola Mundo!"));
	}

	@Test
	public void testList() {
		List<SafeString> list = safeAsList("1", "2", "3");

		assertThat(list.size(), is(3));
		assertThat(list.get(0), is(safeString("1")));
		assertThat(list.get(1), is(safeString("2")));
		assertThat(list.get(2), is(safeString("3")));
	}

}
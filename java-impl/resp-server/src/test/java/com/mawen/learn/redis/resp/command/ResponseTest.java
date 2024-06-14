package com.mawen.learn.redis.resp.command;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ResponseTest {
	private Response response;

	@Before
	public void setUp() {
		response = new Response();
	}

	@Test
	public void testAddBulkStr() {
		assertThat(response.addBulkStr(safeString("test")).toString(), is("$4\r\ntest\r\n"));
	}

	@Test
	public void testAddSimpleStr() {
		assertThat(response.addSimpleStr("test").toString(), is("+test\r\n"));
	}

	@Test
	public void testAddIntString() {
		assertThat(response.addInt(safeString("1")).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntInt() {
		assertThat(response.addInt(1).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntLong() {
		assertThat(response.addInt(1L).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntBooleanTrue() {
		assertThat(response.addInt(true).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntBooleanFalse() {
		assertThat(response.addInt(false).toString(), is(":0\r\n"));
	}

	@Test
	public void testAddError() {
		assertThat(response.addError("ERROR").toString(), is("-ERROR\r\n"));
	}

	@Test
	public void testAddArrayNull() {
		assertThat(response.addArray(null).toString(), is("*0\r\n"));
	}
}
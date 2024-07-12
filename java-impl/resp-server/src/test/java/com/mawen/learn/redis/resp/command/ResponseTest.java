package com.mawen.learn.redis.resp.command;

import org.junit.Before;
import org.junit.Test;

import static com.mawen.learn.redis.resp.protocol.RedisToken.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;
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
		assertThat(response.addBulkStr(safeString("test")).build(), is(string("$4\r\ntest\r\n")));
	}

	@Test
	public void testAddSimpleStr() {
		assertThat(response.addSimpleStr("test").build(), is(status("+test\r\n")));
	}

	@Test
	public void testAddIntInt() {
		assertThat(response.addInt(1).toString(), is(integer(1)));
	}

	@Test
	public void testAddIntBooleanTrue() {
		assertThat(response.addInt(true).build(), is(integer(1)));
	}

	@Test
	public void testAddIntBooleanFalse() {
		assertThat(response.addInt(false).build(), is(integer(0)));
	}

	@Test
	public void testAddError() {
		assertThat(response.addError("ERROR").build(), is(error("ERROR")));
	}

	@Test
	public void testAddArrayNull() {
		assertThat(response.addArray(null).build(), is(array()));
	}

	@Test
	public void testAddArraySafeString() {
		assertThat(response.addArray(asList(safeString("hola"))).build(), is(array(string(safeString("hola")))));
	}

	@Test
	public void testAddArrayString() {
		assertThat(response.addArray(asList("hola")).build(), is(array(string(safeString("hola")))));
	}

	@Test
	public void testAddArrayInteger() {
		assertThat(response.addArray(asList(1)).build(), is(array(integer(1))));
	}

	@Test
	public void testAddArrayBoolean() {
		assertThat(response.addArray(asList(true)).build(), is(array(integer(1))));
	}

	@Test
	public void testAddArrayRedisToken() {
		assertThat(response.addArray(asList(string(safeString("hola")))).build(), is(array(string(safeString("hola")))));
	}
}
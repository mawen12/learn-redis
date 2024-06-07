package com.mawen.learn.redis.basic.command;

import java.util.Arrays;

import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class ResponseTest {

	private Response response;

	@Before
	public void setUp() {
		response = new Response();
	}

	@Test
	public void testAddValue() {
		assertThat(response.addValue(new DatabaseValue(DataType.STRING, "test")).toString(), is("$4\r\ntest\r\n"));
	}

	@Test
	public void testAddValueNull() {
		assertThat(response.addValue(null).toString(), is("$-1\r\n"));
	}

	@Test
	public void testAddBulkStr() {
		assertThat(response.addBulkStr("test").toString(), is("$4\r\ntest\r\n"));
	}

	@Test
	public void testAddSimpleStr() {
		assertThat(response.addSimpleStr("test").toString(), is("+test\r\n"));
	}

	@Test
	public void testAddIntString() {
		assertThat(response.addInt("1").toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntInt() {
		assertThat(response.addInt(1).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddIntBoolean() {
		assertThat(response.addInt(true).toString(), is(":1\r\n"));
	}

	@Test
	public void testAddError() {
		assertThat(response.addError("ERROR").toString(), is("-ERROR\r\n"));
	}

	@Test
	public void testAddArrayValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddArray() {
		assertThat(response.addArray(Arrays.asList("1", "2", "3")).toString(), is("*3\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n"));
	}

}
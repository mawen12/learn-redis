package com.mawen.learn.redis.basic.command;

import java.util.Arrays;
import java.util.List;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Before;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ResponseTest {

	private Response response;

	@Before
	public void setUp() {
		response = new Response();
	}

	@Test
	public void testAddValueString() {
		assertThat(response.addValue(string("test")).toString(), is("$4\r\ntest\r\n"));
	}

	@Test
	public void testAddValueHash() {
		assertThat(response.addValue(hash(entry("key", "value"))).toString(), is("*2\r\n$3\r\nkey\r\n$5\r\nvalue\r\n"));
	}

	@Test
	public void testAddValueList() {
		assertThat(response.addValue(list("a", "b", "c")).toString(), is("*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n"));
	}

	@Test
	public void testAddValueSet() {
		assertThat(response.addValue(set("a", "b", "c")).toString(), is("*3\r\n$1\r\na\r\n$1\r\nb\r\n$1\r\nc\r\n"));
	}

	@Test
	public void testAddValueNull() {
		assertThat(response.addValue(null).toString(), is("$-1\r\n"));
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
	public void testAddArrayValue() {
		List<DatabaseValue> array = Arrays.asList(string("1"), string("2"), string("3"));

		assertThat(response.addArrayValue(array).toString(), is("*3\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n"));
	}

	@Test
	public void testAddArray() {
		assertThat(response.addArray(Arrays.asList(safeString("1"), safeString("2"), safeString("3"), 1, 2, 3)).toString(),
				is("*6\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n:1\r\n:2\r\n:3\r\n"));
	}

	@Test
	public void testAddArrayNull() {
		assertThat(response.addArray(null).toString(), is("*0\r\n"));
	}
}
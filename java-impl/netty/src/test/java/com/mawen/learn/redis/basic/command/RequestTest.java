package com.mawen.learn.redis.basic.command;

import org.junit.Test;

import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RequestTest {

	@Test
	public void testRequest() {
		Request request = new Request(null, null, safeString("a"), safeAsList("1", "2", "3"));

		assertThat(request.getCommand(), is("a"));
		assertThat(request.getLength(), is(3));
		assertThat(request.getParams(), is(safeAsList("1", "2", "3")));
		assertThat(request.getParam(0), is(safeString("1")));
		assertThat(request.getParam(1), is(safeString("2")));
		assertThat(request.getParam(2), is(safeString("3")));
		assertThat(request.getParam(3), is(nullValue()));
		assertThat(request.getOptionalParam(2).isPresent(), is(true));
		assertThat(request.getOptionalParam(2).get(), is(safeString("3")));
		assertThat(request.getOptionalParam(3).isPresent(), is(false));
		assertThat(request.toString(), is("a[3]: [1, 2, 3]"));
	}

}
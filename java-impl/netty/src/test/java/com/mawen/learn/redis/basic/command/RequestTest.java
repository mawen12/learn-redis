package com.mawen.learn.redis.basic.command;

import java.util.Arrays;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RequestTest {

	@Test
	public void testRequest() {
		Request request = new Request();

		request.setCommand("a");

		request.setParams(Arrays.asList("1", "2", "3"));

		assertThat(request.getCommand(), is("a"));
		assertThat(request.getLength(), is(3));
		assertThat(request.getParams(), is(Arrays.asList("1", "2", "3")));
		assertThat(request.getParam(0), is("1"));
		assertThat(request.getParam(1), is("2"));
		assertThat(request.getParam(2), is("3"));
		assertThat(request.toString(), is("a[3]: [1, 2, 3]"));
	}

}
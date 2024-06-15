package com.mawen.learn.redis.resp;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ThreadSafeCacheTest {

	private static final String KEY = "key";

	private ThreadSafeCache<String, String> cache;

	@Mock
	private Consumer<String> consumer;

	@Before
	public void setUp() {
		cache = new ThreadSafeCache<>();
	}

	@Test
	public void getOrCreate() {
		String result = cache.get(KEY, Function.identity(), System.out::println);

		assertThat(result,equalTo(KEY));
	}

	@Test
	public void callbackIsCalled() {
		cache.get(KEY, Function.identity(), consumer);

		verify(consumer, times(1)).accept(KEY);
	}

	@Test
	public void callbackIsCalledOnlyOnce() {
		cache.get(KEY, Function.identity(), consumer);
		cache.get(KEY, Function.identity(), consumer);

		verify(consumer, times(1)).accept(KEY);
	}

}
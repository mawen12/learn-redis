package com.mawen.learn.redis.basic.data;

import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DatabaseKeyTest {

	@Test
	public void testExpired() throws InterruptedException {
		DatabaseKey nonExpiredKey = safeKey("hola");
		assertThat(nonExpiredKey.isExpired(), is(false));
		assertThat(nonExpiredKey.timeToLive(), is(-1L));

		DatabaseKey expiredKey = safeKey("hola", 1);
		assertThat(expiredKey.isExpired(), is(false));
		assertThat(expiredKey.timeToLive(), is(greaterThan(0L)));
		Thread.sleep(1100);
		assertThat(expiredKey.isExpired(), is(true));
		assertThat(expiredKey.timeToLive(), is(-2L));
	}

}
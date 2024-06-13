package com.mawen.learn.redis.basic.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DatabaseKeyTest {

	@Test
	public void testExpired() throws InterruptedException {
		DatabaseKey nonExpiredKey = DatabaseKey.safeKey("hola");
		assertThat(nonExpiredKey.isExpired(), is(false));

		DatabaseKey expiredKey = DatabaseKey.ttlKey("hola", 500);
		assertThat(expiredKey.isExpired(), is(false));
		Thread.sleep(1000);
		assertThat(expiredKey.isExpired(), is(true));
	}

}
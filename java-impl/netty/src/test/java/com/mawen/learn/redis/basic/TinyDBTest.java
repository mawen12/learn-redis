package com.mawen.learn.redis.basic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TinyDBTest {

	private final TinyDB db = new TinyDB();

	private final Thread server = new Thread(db::start);

	@Before
	public void setUp() throws Exception {
		server.start();
		Thread.sleep(1000);
	}

	@After
	public void tearDown() throws Exception {
		db.stop();
	}

	@Test
	public void testCommands() {
		try (Jedis jedis = new Jedis("localhost", 7081)) {
			assertThat(jedis.ping(), is("PONG"));
			assertThat(jedis.echo("Hi!"), is("Hi!"));
			assertThat(jedis.set("a", "1"), is("OK"));
			assertThat(jedis.strlen("a"), is(1L));
			assertThat(jedis.strlen("b"), is(0L));
			assertThat(jedis.exists("a"), is(true));
			assertThat(jedis.exists("b"), is(false));
			assertThat(jedis.get("a"), is("1"));
			assertThat(jedis.get("b"), is(nullValue()));
			assertThat(jedis.getSet("a", "2"), is("1"));
			assertThat(jedis.get("a"), is("2"));
			assertThat(jedis.del("a"), is(1L));
			assertThat(jedis.get("a"), is(nullValue()));
		}
	}

	@Test(timeout = 1000)
	public void testLoad1000() {
		loadTest(1000);
	}

	@Test(timeout = 2000)
	public void testLoad10000() {
		loadTest(10000);
	}

	@Test(timeout = 20000)
	public void testLoad100000() {
		loadTest(100000);
	}

	private void loadTest(int times) {
		try (Jedis jedis = new Jedis("localhost", 7081)) {
			for (int i = 0; i < times; i++) {
				jedis.set(key(i), value(i));
			}
		}
	}

	private String key(int i) {
		return "key" + i;
	}

	private String value(int i) {
		return "value" + i;
	}
}
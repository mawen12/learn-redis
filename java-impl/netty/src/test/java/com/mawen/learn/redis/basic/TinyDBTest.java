package com.mawen.learn.redis.basic;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

public class TinyDBTest {

	@Rule
	public final TinyDBRule rule = new TinyDBRule();

	@Test
	public void testCommands() {
		try (Jedis jedis = createClientConnection()) {
			assertThat(jedis.ping(), equalTo("PONG"));
			assertThat(jedis.echo("Hi!"), equalTo("Hi!"));
			assertThat(jedis.set("a", "1"), equalTo("OK"));
			assertThat(jedis.strlen("a"), equalTo(1L));
			assertThat(jedis.strlen("b"), equalTo(0L));
			assertThat(jedis.exists("a"), equalTo(true));
			assertThat(jedis.exists("b"), equalTo(false));
			assertThat(jedis.get("a"), equalTo("1"));
			assertThat(jedis.get("b"), nullValue());
			assertThat(jedis.getSet("a", "2"), equalTo("1"));
			assertThat(jedis.get("a"), equalTo("2"));
			assertThat(jedis.del("a"), equalTo(1L));
			assertThat(jedis.get("a"), nullValue());
			assertThat(jedis.quit(), equalTo("OK"));
		}
	}

	@Test
	public void testPipeline() {
		try (Jedis jedis = createClientConnection()) {
			Pipeline p = jedis.pipelined();
			p.ping(); // 0
			p.echo("Hi!"); // 1
			p.set("a", "1"); // 2
			p.strlen("a"); // 3
			p.strlen("b"); // 4
			p.exists("a"); // 5
			p.exists("b"); // 6
			p.get("a"); // 7
			p.get("b"); // 8
			p.getSet("a", "2"); // 9
			p.get("a"); // 10
			p.del("a"); // 11
			p.get("a"); // 12

			Iterator<Object> result = p.syncAndReturnAll().iterator();
			assertThat(result.next(), equalTo("PONG"));
			assertThat(result.next(), equalTo("Hi!"));
			assertThat(result.next(), equalTo("OK"));
			assertThat(result.next(), equalTo(1L));
			assertThat(result.next(), equalTo(0L));
			assertThat(result.next(), equalTo(true));
			assertThat(result.next(), equalTo(false));
			assertThat(result.next(), equalTo("1"));
			assertThat(result.next(), nullValue());
			assertThat(result.next(), equalTo("1"));
			assertThat(result.next(), equalTo("2"));
			assertThat(result.next(), equalTo(1L));
			assertThat(result.next(), nullValue());

			jedis.quit();
		}
	}

	@Test
	public void testLoad1000() {
		loadTest(1000);
	}

	@Test
	public void testLoad10000() {
		loadTest(10000);
	}

	@Test
	public void testLoad100000() {
		loadTest(100000);
	}

	private void loadTest(int times) {
		try (Jedis jedis = createClientConnection()) {
			long start = System.nanoTime();
			for (int i = 0; i < times; i++) {
				jedis.set(key(i), value(i));
			}
			jedis.quit();
			assertThat((System.nanoTime() - start) / times, is(lessThan(1000000L)));
		}
	}

	private Jedis createClientConnection() {
		return new Jedis(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT);
	}

	private String key(int i) {
		return "key" + i;
	}

	private String value(int i) {
		return "value" + i;
	}
}
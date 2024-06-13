package com.mawen.learn.redis.basic;

import java.util.List;

import org.hamcrest.number.OrderingComparison;
import org.junit.Rule;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TinyDBTest {

	@Rule
	public final TinyDBRule rule = new TinyDBRule();

	@Test
	public void testCommands() {
		try (Jedis jedis = new Jedis(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT)) {
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
			assertThat(jedis.quit(), is("OK"));
		}
	}

	@Test
	public void testPipeline() {
		try (Jedis jedis = new Jedis(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT)) {
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

			List<Object> result = p.syncAndReturnAll();
			assertThat(result.get(0), is("PONG"));
			assertThat(result.get(1), is("Hi!"));
			assertThat(result.get(2), is("OK"));
			assertThat(result.get(3), is(1L));
			assertThat(result.get(4), is(0L));
			assertThat(result.get(5), is(true));
			assertThat(result.get(6), is(false));
			assertThat(result.get(7), is("1"));
			assertThat(result.get(8), is(nullValue()));
			assertThat(result.get(9), is("1"));
			assertThat(result.get(10), is("2"));
			assertThat(result.get(11), is(1L));
			assertThat(result.get(12), is(nullValue()));

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
		long start = System.nanoTime();
		try (Jedis jedis = new Jedis(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT)) {
			for (int i = 0; i < times; i++) {
				jedis.set(key(i), value(i));
			}
			jedis.quit();
		}
		assertThat((System.nanoTime() - start) / times, is(OrderingComparison.lessThan(1000000L)));
	}

	private String key(int i) {
		return "key" + i;
	}

	private String value(int i) {
		return "value" + i;
	}
}
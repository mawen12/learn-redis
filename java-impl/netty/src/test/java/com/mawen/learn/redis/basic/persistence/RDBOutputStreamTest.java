package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.util.HashMap;

import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Before;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RDBOutputStreamTest {

	private ByteBufferOutputStream baos;
	private RDBOutputStream out;

	@Before
	public void setUp() {
		baos = new ByteBufferOutputStream();
		out = new RDBOutputStream(baos);
	}

	/**
	 * <pre>
	 *
	 * </pre>
	 */
	@Test
	public void testStartEnd() throws IOException {
		out.preamble(3);
		out.select(0);
		out.end();

		assertThat(toHexString(baos.toByteArray()), is("524544495330303033FE00FF77DE0394AC9D23EA"));
	}

	@Test
	public void testString() throws IOException {
		out.database(database().add("a", string("test")).build());

		assertThat(toHexString(baos.toByteArray()), is("0001610474657374"));
	}

	@Test
	public void testList() throws IOException {
		out.database(database().add("a", list("test")).build());

		assertThat(toHexString(baos.toByteArray()), is("010161010474657374"));
	}

	@Test
	public void testSet() throws IOException {
		out.database(database().add("a", set("test")).build());

		assertThat(toHexString(baos.toByteArray()), is("020161010474657374"));
	}

	@Test
	public void testSortedSet() throws IOException {
		out.database(database().add("a", zset(score(1.0, "test"))).build());

		assertThat(toHexString(baos.toByteArray()), is("03016101047465737403312E30"));
	}

	@Test
	public void testHash() throws IOException {
		out.database(database().add("a", hash(entry("1", "test"))).build());

		assertThat(toHexString(baos.toByteArray()), is("0401610101310474657374"));
	}

	@Test
	public void testAll() throws IOException {
		out.preamble(3);
		out.select(0);
		out.database(database().add("a", string("test")).build());
		out.select(1);
		out.database(database().add("a", list("test")).build());
		out.select(2);
		out.database(database().add("a", set("test")).build());
		out.select(3);
		out.database(database().add("a", zset(score(1.0, "test"))).build());
		out.select(4);
		out.database(database().add("a", hash(entry("1", "test"))).build());
		out.end();

		System.out.println(HexUtil.toHexString(baos.toByteArray()));

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("524544495330303033FE000001610474657374FE01010161010474657374FE02020161010474657374FE0303016101047465737403312E30FE040401610101310474657374FFE5C54809420836EA"));
	}

	private static DatabaseBuilder database() {
		return new DatabaseBuilder();
	}

	private static class DatabaseBuilder {

		private final IDatabase db = new Database(new HashMap<>());

		public DatabaseBuilder add(String key, DatabaseValue value) {
			db.put(key, value);
			return this;
		}

		public IDatabase build() {
			return db;
		}
	}
}
package com.mawen.learn.redis.basic.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Before;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RDBOutputStreamTest {

	private ByteArrayOutputStream baos;
	private RDBOutputStream out;

	@Before
	public void setUp() {
		baos = new ByteArrayOutputStream();
		out = new RDBOutputStream(baos);
	}

	/**
	 * <pre>
	 *
	 * </pre>
	 *
	 * @throws IOException
	 */
	@Test
	public void testStartEnd() throws IOException {
		out.preamble(3);
		out.end();

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("52454449533030303033FF66A145766BC31005"));
	}

	@Test
	public void testString() throws IOException {
		out.select(0);
		out.database(database().add("a", string("test")).build());

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("FE000001610474657374"));
	}

	@Test
	public void testList() throws IOException {
		out.select(0);
		out.database(database().add("a", list("test")).build());

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("FE00010161010474657374"));
	}

	@Test
	public void testSet() throws IOException {
		out.select(0);
		out.database(database().add("a", set("test")).build());

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("FE00020161010474657374"));
	}

	@Test
	public void testHash() throws IOException {
		out.select(0);
		out.database(database().add("a", hash(entry("1", "test"))).build());

		assertThat(HexUtil.toHexString(baos.toByteArray()), is("FE000401610101310474657374"));
	}

	public static DatabaseBuilder database() {
		return new DatabaseBuilder();
	}

	private static class DatabaseBuilder {

		private IDatabase db = new Database(new HashMap<>());

		public DatabaseBuilder add(String key, DatabaseValue value) {
			db.put(key, value);
			return this;
		}

		public IDatabase build() {
			return db;
		}
	}
}
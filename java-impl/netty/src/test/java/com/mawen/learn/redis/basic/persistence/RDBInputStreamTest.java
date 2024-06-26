package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.entry;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.list;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.score;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.set;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RDBInputStreamTest {

	@Test
	public void testEmpty() throws IOException {
		RDBInputStream in = new RDBInputStream(array("524544495330303033FE00FF77DE0394AC9D23EA"));

		Map<Integer, IDatabase> databases = in.parse();

		assertThat(databases.size(), is(1));
	}

	@Test
	public void testAll() throws IOException {
		RDBInputStream in = new RDBInputStream(array("524544495330303033FE000001610474657374FE01010161010474657374FE02020161010474657374FE0303016101047465737403312E30FE040401610101310474657374FE05FC00000000000000010001610474657374FFA9D1F09C463A7043"));

		Map<Integer, IDatabase> databases = in.parse();

		assertThat(databases.size(), is(6));

		assertDB(databases.get(0), string("test"));
		assertDB(databases.get(1), list("test"));
		assertDB(databases.get(2), set("test"));
		assertDB(databases.get(3), zset(score(1.0, "test")));
		assertDB(databases.get(4), hash(entry("1", "test")));
		assertThat(databases.get(5), notNullValue());
		assertThat(databases.get(5).isEmpty(), is(true));
	}

	private InputStream array(String string) {
		return new ByteBufferInputStream(HexUtil.toByteArray(string));
	}

	private void assertDB(IDatabase db, DatabaseValue value) {
		assertThat(db, notNullValue());
		assertThat(db.get(safeKey("a")), is(value));
	}
}
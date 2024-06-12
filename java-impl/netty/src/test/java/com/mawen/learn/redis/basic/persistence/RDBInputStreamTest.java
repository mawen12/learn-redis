package com.mawen.learn.redis.basic.persistence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
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
		RDBInputStream in = new RDBInputStream(array("524544495330303033FE000001610474657374FE01010161010474657374FE02020161010474657374FE0303016101047465737403312E30FE040401610101310474657374FFE5C54809420836EA"));

		Map<Integer, IDatabase> databases = in.parse();

		assertThat(databases.size(), is(5));

		assertDB(databases.get(0), string("test"));
		assertDB(databases.get(1), list("test"));
		assertDB(databases.get(2), set("test"));
		assertDB(databases.get(3), zset(score(1.0, "test")));
		assertDB(databases.get(4), hash(entry("1", "test")));
	}

	private ByteArrayInputStream array(String string) {
		return new ByteArrayInputStream(HexUtil.toByteArray(string));
	}

	private void assertDB(IDatabase db, DatabaseValue value) {
		assertThat(db, notNullValue());
		assertThat(db.get("a"), is(value));
	}
}
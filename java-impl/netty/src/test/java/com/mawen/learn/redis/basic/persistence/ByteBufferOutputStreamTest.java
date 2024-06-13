package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;

import org.junit.Test;

import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ByteBufferOutputStreamTest {

	@Test
	public void testStream() throws IOException {
		ByteBufferOutputStream out = new ByteBufferOutputStream(10);

		out.write(9);
		out.write(toByteArray("486F6C61206D756E646F21"));

		assertThat(HexUtil.toHexString(out.toByteArray()), is("09486F6C61206D756E646F21"));

		out.close();
	}
}
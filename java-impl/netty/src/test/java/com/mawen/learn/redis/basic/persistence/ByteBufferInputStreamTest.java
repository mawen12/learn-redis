package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;

import com.mawen.learn.redis.basic.redis.SafeString;
import org.junit.Test;

import static com.mawen.learn.redis.basic.persistence.HexUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ByteBufferInputStreamTest {

	@Test
	public void testStream() throws IOException {

		ByteBufferInputStream in = new ByteBufferInputStream(toByteArray("09486F6C61206D756E646F21"));

		assertThat(in.read(), is(9));

		byte[] array = new byte[in.available()];
		int readed = in.read(array);

		assertThat(readed, is(array.length));
		assertThat(toHexString(array), is("486F6C61206D756E646F21"));

		in.close();
	}

}
package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class ByteBufferInputStream extends InputStream {

	private final ByteBuffer buffer;

	public ByteBufferInputStream(byte[] array) {
		this.buffer = ByteBuffer.wrap(array);
	}

	@Override
	public int read() throws IOException {
		if (!buffer.hasRemaining()) {
			return -1;
		}
		return buffer.get() & 0xFF;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int available = Math.min(len, buffer.remaining());
		buffer.get(b, off, available);
		return available;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}
}

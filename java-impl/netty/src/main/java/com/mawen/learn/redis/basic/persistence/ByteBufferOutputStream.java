package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class ByteBufferOutputStream extends OutputStream {

	private final ByteBuffer buffer;

	public ByteBufferOutputStream() {
		this(1024);
	}

	public ByteBufferOutputStream(int capacity) {
		this.buffer = ByteBuffer.allocate(capacity);
	}

	@Override
	public void write(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.put(b, off, len);
	}

	public byte[] toByteArray() {
		byte[] array = new byte[buffer.position()];
		buffer.rewind();
		buffer.get(array);
		return array;
	}
}

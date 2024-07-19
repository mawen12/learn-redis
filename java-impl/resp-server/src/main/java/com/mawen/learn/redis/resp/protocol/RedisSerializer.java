package com.mawen.learn.redis.resp.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/7/12
 */
public class RedisSerializer {

	private static final byte ARRAY = '*';
	private static final byte ERROR = '-';
	private static final byte INTEGER = ':';
	private static final byte SIMPLE_STRING = '+';
	private static final byte BULK_STRING = '$';

	private static final byte[] DELIMITER = new byte[]{'\r', '\n'};
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private ByteBufferBuilder builder = new ByteBufferBuilder();

	public byte[] encodedToken(RedisToken msg) throws IOException {
		switch (msg.getType()) {
			case STRING:
				addBulkStr(msg.<SafeString>getValue());
				break;
			case STATUS:
				addSimpleStr(msg.<SafeString>getValue());
				break;
			case INTEGER:
				addInt(msg.<Integer>getValue());
				break;
			case ERROR:
				addError(msg.<SafeString>getValue());
				break;
			case ARRAY:
				addArray(msg.<List<RedisToken>>getValue());
				break;
			case UNKNOWN:
				break;
		}
		return builder.build();
	}

	private void addBulkStr(SafeString str) {
		if (str != null) {
			builder.append(BULK_STRING).append(str.length()).append(DELIMITER).append(str);
		}
		else {
			builder.append(BULK_STRING).append(-1);
		}
		builder.append(DELIMITER);
	}

	private void addSimpleStr(SafeString str) {
		builder.append(SIMPLE_STRING).append(str.getBytes()).append(DELIMITER);
	}

	private void addInt(int value) {
		builder.append(INTEGER).append(value).append(DELIMITER);
	}

	private void addError(SafeString str) {
		builder.append(ERROR).append(str.getBytes()).append(DELIMITER);
	}

	private void addArray(Collection<RedisToken> array) throws IOException {
		if (array != null) {
			builder.append(ARRAY).append(array.size()).append(DELIMITER);
			for (RedisToken token : array) {
				builder.append(new RedisSerializer().encodedToken(token));
			}
		}
		else {
			builder.append(ARRAY).append(0).append(DELIMITER);
		}
	}

	private static class ByteBufferBuilder {
		private static final int INITIAL_CAPACITY = 1024;

		private ByteBuffer buffer = ByteBuffer.allocate(INITIAL_CAPACITY);

		public ByteBufferBuilder append(int i) {
			append(String.valueOf(i));
			return this;
		}

		public ByteBufferBuilder append(String str) {
			append(str.getBytes(DEFAULT_CHARSET));
			return this;
		}

		public ByteBufferBuilder append(SafeString str) {
			append(str.getBytes());
			return this;
		}

		private ByteBufferBuilder append(byte[] buf) {
			ensureCapacity(buf.length);
			buffer.put(buf);
			return this;
		}

		public ByteBufferBuilder append(byte b) {
			ensureCapacity(1);
			buffer.put(b);
			return this;
		}

		private void ensureCapacity(int len) {
			if (buffer.remaining() < len) {
				growBuffer(len);
			}
		}

		private void growBuffer(int len) {
			int capacity = buffer.capacity() + Math.max(len, INITIAL_CAPACITY);
			buffer = ByteBuffer.allocate(capacity).put(build());
		}

		public byte[] build() {
			byte[] array = new byte[buffer.position()];
			buffer.rewind();
			buffer.get(array);
			return array;
		}
	}
}

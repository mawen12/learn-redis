package com.mawen.learn.redis.basic.command;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.redis.SafeString;

import static com.mawen.learn.redis.basic.redis.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Response implements IResponse {

	private static final byte ARRAY = '*';
	private static final byte ERROR = '-';
	private static final byte INTEGER = ':';
	private static final byte SIMPLE_STRING = '+';
	private static final byte BULK_STRING = '$';

	private static final byte[] DELIMITER = new byte[]{'\r', '\n'};

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private boolean exit;

	private final ByteBufferBuilder builder = new ByteBufferBuilder();

	@Override
	public IResponse addValue(DatabaseValue value) {
		if (value != null) {
			switch (value.getType()) {
				case STRING:
					addBulkStr(value.getValue());
					break;
				case HASH:
					Map<String, String> map = value.getValue();
					List<Object> list = new LinkedList<>();
					map.forEach((k, v) -> {
						list.add(safeString(k));
						list.add(safeString(v));
					});
					addArray(list);
					break;
				case LIST:
				case SET:
				case ZSET:
					addArray(value.getValue());
					break;
				default:
					break;
			}
		}
		else {
			addBulkStr(null);
		}
		return this;
	}

	@Override
	public IResponse addBulkStr(SafeString str) {
		if (str != null) {
			builder.append(BULK_STRING).append(str.length()).append(DELIMITER).append(str);
		}
		else {
			builder.append(BULK_STRING).append(-1);
		}
		builder.append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addSimpleStr(String str) {
		builder.append(SIMPLE_STRING).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(SafeString str) {
		builder.append(INTEGER).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(int value) {
		builder.append(INTEGER).append(value).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(boolean value) {
		builder.append(INTEGER).append(value ? "1" : "0").append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addError(String str) {
		builder.append(ERROR).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addArrayValue(Collection<DatabaseValue> array) {
		if (array != null) {
			builder.append(ARRAY).append(array.size()).append(DELIMITER);
			for (DatabaseValue value : array) {
				addValue(value);
			}
		}
		else {
			builder.append(ARRAY).append(0).append(DELIMITER);
		}
		return this;
	}

	@Override
	public IResponse addArray(Collection<?> array) {
		if (array != null) {
			builder.append(ARRAY).append(array.size()).append(DELIMITER);
			for (Object value : array) {
				if (value instanceof Integer) {
					addInt((Integer) value);
				}
				else if (value instanceof SafeString) {
					addBulkStr((SafeString) value);
				}
				else if (value instanceof String) {
					addSimpleStr((String) value);
				}
			}
		}
		else {
			builder.append(ARRAY).append(0).append(DELIMITER);
		}
		return this;
	}

	@Override
	public void exit() {
		this.exit = true;
	}

	@Override
	public boolean isExit() {
		return exit;
	}

	public byte[] getBytes() {
		return builder.build();
	}

	@Override
	public String toString() {
		return new String(getBytes(), StandardCharsets.UTF_8);
	}

	private static class ByteBufferBuilder {

		private ByteBuffer buffer = ByteBuffer.allocate(1024);

		public ByteBufferBuilder append(int i) {
			append(String.valueOf(i));
			return this;
		}

		public ByteBufferBuilder append(byte b) {
			ensureCapacity(1);
			buffer.put(b);
			return this;
		}

		public ByteBufferBuilder append(byte[] buf) {
			ensureCapacity(buf.length);
			buffer.put(buf);
			return this;
		}

		public ByteBufferBuilder append(String str) {
			append(DEFAULT_CHARSET.encode(str));
			return this;
		}

		public ByteBufferBuilder append(SafeString str) {
			append(str.getBuffer());
			return this;
		}

		public ByteBufferBuilder append(ByteBuffer b) {
			ensureCapacity(b.capacity());
			buffer.put(b);
			return this;
		}

		private void ensureCapacity(int len) {
			if (buffer.remaining() < len) {
				buffer = ByteBuffer.allocate(buffer.capacity() + 1024).put(build());
			}
		}

		public byte[] build() {
			byte[] array = new byte[buffer.position()];
			buffer.rewind();
			buffer.get(array);
			return array;
		}
	}
}

package com.mawen.learn.redis.basic.command;

import java.io.IOError;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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

	public static final byte[] DELIMITER = new byte[]{'\r', '\n'};

	private boolean exit;

	private final ByteArrayBuilder builder = new ByteArrayBuilder();

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

	private static class ByteArrayBuilder {

		private final ByteBuffer buffer = ByteBuffer.allocate(1024);

		public ByteArrayBuilder append(int i) {
			append(String.valueOf(i));
			return this;
		}

		public ByteArrayBuilder append(byte b) {
			buffer.put(b);
			return this;
		}

		public ByteArrayBuilder append(byte[] buf) {
			buffer.put(buf);
			return this;
		}

		public ByteArrayBuilder append(String str) {
			try {
				buffer.put(str.getBytes("UTF-8"));
			}
			catch (UnsupportedEncodingException e) {
				throw new IOError(e);
			}
			return this;
		}

		public ByteArrayBuilder append(SafeString str) {
			buffer.put(str.getBytes());
			return this;
		}

		public byte[] build() {
			byte[] array = new byte[buffer.position()];
			buffer.rewind();
			buffer.get(array);
			return array;
		}
	}
}

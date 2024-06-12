package com.mawen.learn.redis.basic.redis;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisToken {

	private static final String SEPARATOR = "=>";

	private final RedisTokenType type;

	private final Object value;

	public RedisToken(RedisTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public RedisTokenType getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}

	@Override
	public String toString() {
		return type + SEPARATOR + value;
	}

	public static class StatusRedisToken extends RedisToken {
		public StatusRedisToken(String value) {
			super(RedisTokenType.STATUS, value);
		}
	}

	public static class IntegerRedisToken extends RedisToken {
		public IntegerRedisToken(Integer value) {
			super(RedisTokenType.INTEGER, value);
		}
	}

	public static class StringRedisToken extends RedisToken {
		public StringRedisToken(SafeString value) {
			super(RedisTokenType.STRING, value);
		}
	}

	public static class ArrayRedisToken extends RedisToken {
		public ArrayRedisToken(RedisArray value) {
			super(RedisTokenType.ARRAY, value);
		}

		public int size() {
			return this.<RedisArray>getValue().size();
		}
	}

	public static class ErrorRedisToken extends RedisToken {
		public ErrorRedisToken(String value) {
			super(RedisTokenType.ERROR, value);
		}
	}

	public static class UnknownRedisToken extends RedisToken {
		public UnknownRedisToken(String value) {
			super(RedisTokenType.UNKNOWN, value);
		}
	}
}

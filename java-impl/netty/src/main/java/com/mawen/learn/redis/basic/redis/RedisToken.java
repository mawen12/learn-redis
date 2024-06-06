package com.mawen.learn.redis.basic.redis;

import java.util.List;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisToken<T> {

	private static final String SEPARATOR = "=>";

	private final RedisTokenType type;

	private final T value;

	public RedisToken(RedisTokenType type, T value) {
		this.type = type;
		this.value = value;
	}

	public RedisTokenType getType() {
		return type;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return type + SEPARATOR + value;
	}

	public static class StatusRedisToken extends RedisToken<String> {
		public StatusRedisToken(String value) {
			super(RedisTokenType.STATUS, value);
		}
	}

	public static class IntegerRedisToken extends RedisToken<Integer> {
		public IntegerRedisToken(Integer value) {
			super(RedisTokenType.INTEGER, value);
		}
	}

	public static class StringRedisToken extends RedisToken<String> {
		public StringRedisToken(String value) {
			super(RedisTokenType.STRING, value);
		}
	}

	public static class ArrayRedisToken extends RedisToken<List<RedisToken<?>>> {
		public ArrayRedisToken(List<RedisToken<?>> value) {
			super(RedisTokenType.ARRAY, value);
		}

		public int size() {
			return getValue().size();
		}
	}

	public static class ErrorRedisToken extends RedisToken<String> {
		public ErrorRedisToken(String value) {
			super(RedisTokenType.ERROR, value);
		}
	}

	public static class UnknownRedisToken extends RedisToken<String> {
		public UnknownRedisToken(String value) {
			super(RedisTokenType.UNKNOWN, value);
		}
	}
}

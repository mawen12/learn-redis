package com.mawen.learn.redis.resp.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import static tonivade.equalizer.Equalizer.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public abstract class RedisToken {

	private static final String SEPARATOR = "=>";

	private final RedisTokenType type;

	private final Object value;

	public RedisToken(RedisTokenType type, Object value) {
		this.type = requireNonNull(type);
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
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public boolean equals(Object o) {
		return equalizer(this)
				.append((one, other) -> Objects.equals(one.value, other.value))
				.applyTo(o);
	}

	@Override
	public String toString() {
		return type + SEPARATOR + value;
	}

	public static RedisToken string(String str) {
		return new StringRedisToken(safeString(str));
	}

	public static RedisToken string(SafeString str) {
		return new StringRedisToken(str);
	}

	public static RedisToken status(String str) {
		return new StatusRedisToken(safeString(str));
	}

	public static RedisToken error(String str) {
		return new ErrorRedisToken(safeString(str));
	}

	public static RedisToken integer(int value) {
		return new IntegerRedisToken(value);
	}

	public static RedisToken array(RedisToken... values) {
		return new ArrayRedisToken(asList(values));
	}

	public static RedisToken array(Collection<RedisToken> values) {
		return new ArrayRedisToken(values);
	}

	static class UnknownRedisToken extends RedisToken {
		public UnknownRedisToken(SafeString value) {
			super(RedisTokenType.UNKNOWN, value);
		}
	}

	static class StringRedisToken extends RedisToken {
		public StringRedisToken(SafeString value) {
			super(RedisTokenType.STRING, value);
		}
	}

	static class StatusRedisToken extends RedisToken {
		public StatusRedisToken(SafeString value) {
			super(RedisTokenType.STATUS, value);
		}
	}

	static class ErrorRedisToken extends RedisToken {
		public ErrorRedisToken(SafeString value) {
			super(RedisTokenType.ERROR, value);
		}
	}

	static class IntegerRedisToken extends RedisToken {
		public IntegerRedisToken(Integer value) {
			super(RedisTokenType.INTEGER, value);
		}
	}

	static class ArrayRedisToken extends RedisToken {
		public ArrayRedisToken(Collection<RedisToken> value) {
			super(RedisTokenType.ARRAY, Collections.unmodifiableList(new ArrayList<>(value)));
		}

		public int size() {
			return this.<List<RedisToken>>getValue().size();
		}
	}
}

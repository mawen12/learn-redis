package com.mawen.learn.redis.basic.data;

import java.util.Objects;

import com.mawen.learn.redis.basic.redis.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class DatabaseKey {

	private SafeString value;

	public DatabaseKey(SafeString value) {
		super();
		this.value = value;
	}

	public SafeString getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		DatabaseKey that = (DatabaseKey) obj;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public static DatabaseKey safeKey(SafeString str) {
		return new DatabaseKey(str);
	}

	public static DatabaseKey safeKey(String str) {
		return new DatabaseKey(SafeString.safeString(str));
	}
}

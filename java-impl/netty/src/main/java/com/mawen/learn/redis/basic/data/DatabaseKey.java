package com.mawen.learn.redis.basic.data;

import java.util.Objects;

import com.mawen.learn.redis.basic.redis.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class DatabaseKey implements Comparable<DatabaseKey> {

	private Long expiredAt;
	private SafeString value;

	public DatabaseKey(SafeString value) {
		this(value, 0);
	}

	public DatabaseKey(SafeString value, long ttl) {
		super();
		this.value = value;
		if (ttl > 0) {
			this.expiredAt = System.currentTimeMillis() + ttl;
		}
	}

	public SafeString getValue() {
		return value;
	}

	public boolean isExpired() {
		if (expiredAt != null) {
			long now = System.currentTimeMillis();
			return now > expiredAt;
		}
		return false;
	}

	@Override
	public int compareTo(DatabaseKey o) {
		return value.compareTo(o.getValue());
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

	public static DatabaseKey ttlKey(SafeString str, long ttl) {
		return new DatabaseKey(str, ttl);
	}

	public static DatabaseKey safeKey(String str) {
		return new DatabaseKey(SafeString.safeString(str));
	}

	public static DatabaseKey ttlKey(String str, long ttl) {
		return new DatabaseKey(SafeString.safeString(str), ttl);
	}
}

package com.mawen.learn.redis.basic.data;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.mawen.learn.redis.basic.redis.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class DatabaseKey implements Comparable<DatabaseKey> {

	private final Long expiredAt;

	private final SafeString value;

	public DatabaseKey(SafeString value, Long expiredAt) {
		super();
		this.value = value;
		this.expiredAt = expiredAt;
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

	public long timeToLive() {
		if (expiredAt != null) {
			long ttl = expiredAt - System.currentTimeMillis();
			return ttl < 0 ? -2 : ttl;
		}
		return -1;
	}

	public Long expiredAt() {
		return expiredAt;
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
		return new DatabaseKey(str, null);
	}

	public static DatabaseKey safeKey(SafeString str, long ttlMillis) {
		return new DatabaseKey(str, System.currentTimeMillis() + ttlMillis);
	}

	public static DatabaseKey safeKey(SafeString str, int ttlSeconds) {
		return safeKey(str, TimeUnit.SECONDS.toMillis(ttlSeconds));
	}
}

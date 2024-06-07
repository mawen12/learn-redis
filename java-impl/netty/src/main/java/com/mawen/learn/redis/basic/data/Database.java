package com.mawen.learn.redis.basic.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Database implements IDatabase {

	private final StampedLock lock = new StampedLock();

	private final Map<String, DatabaseValue> cache;

	public Database(Map<String, DatabaseValue> cache) {
		this.cache = cache;
	}

	@Override
	public int size() {
		long stamp = lock.readLock();
		try {
			return cache.size();
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public boolean isEmpty() {
		long stamp = lock.readLock();
		try {
			return cache.isEmpty();
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public boolean containsKey(Object key) {
		long stamp = lock.readLock();
		try {
			return cache.containsKey(key);
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public boolean containsValue(Object value) {
		long stamp = lock.readLock();
		try {
			return cache.containsValue(value);
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public DatabaseValue get(Object key) {
		long optimistic = lock.tryOptimisticRead();
		DatabaseValue value = cache.get(key);

		if (!lock.validate(optimistic)) {
			long stamp = lock.readLock();
			try {
				value = cache.get(key);
			}
			finally {
				lock.unlockRead(stamp);
			}
		}

		return value;
	}

	@Override
	public DatabaseValue put(String key, DatabaseValue value) {
		long stamp = lock.writeLock();
		try {
			return cache.put(key, value);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public DatabaseValue remove(Object key) {
		long stamp = lock.writeLock();
		try {
			return cache.remove(key);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends DatabaseValue> m) {
		long stamp = lock.writeLock();
		try {
			cache.putAll(m);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public void clear() {
		long stamp = lock.writeLock();
		try {
			cache.clear();
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public Set<String> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<DatabaseValue> values() {
		return cache.values();
	}

	@Override
	public Set<Entry<String, DatabaseValue>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public DatabaseValue putIfAbsent(String key, DatabaseValue value) {
		long stamp = lock.writeLock();
		try {
			return cache.putIfAbsent(key, value);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public DatabaseValue merge(String key, DatabaseValue value, BiFunction<? super DatabaseValue, ? super DatabaseValue, ? extends DatabaseValue> remappingFunction) {
		long stamp = lock.writeLock();
		try {
			return cache.merge(key, value, remappingFunction);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public boolean isType(String key, DataType type) {
		long stamp = lock.readLock();
		try {
			return cache.getOrDefault(key, new DatabaseValue(type)).getType() == type;
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public boolean rename(String from, String to) {
		long stamp = lock.writeLock();
		try {
			DatabaseValue value = cache.remove(from);
			if (value != null) {
				cache.put(to, value);
				return true;
			}
			return false;
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}
}

package com.mawen.learn.redis.basic.data;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Database implements IDatabase, Runnable {

	private final StampedLock lock = new StampedLock();

	private final NavigableMap<DatabaseKey, DatabaseValue> cache;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public Database() {
		this(new TreeMap<>());
	}

	public Database(NavigableMap<DatabaseKey, DatabaseValue> cache) {
		this.cache = cache;
		this.executor.scheduleAtFixedRate(this, 5, 5, TimeUnit.MINUTES);
	}

	@Override
	public void run() {
		Set<DatabaseKey> toRemove = keySet().stream().filter(DatabaseKey::isExpired).collect(toSet());

		if (!toRemove.isEmpty()) {
			long stamp = lock.writeLock();
			try {
				for (DatabaseKey key : toRemove) {
					cache.remove(key);
				}
			}
			finally {
				lock.unlockWrite(stamp);
			}
		}
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
		Entry<DatabaseKey, DatabaseValue> entry = null;

		if (key instanceof DatabaseKey) {
			long optimistic = lock.tryOptimisticRead();
			entry = cache.ceilingEntry((DatabaseKey) key);

			if (!lock.validate(optimistic)) {
				long stamp = lock.readLock();
				try {
					entry = cache.ceilingEntry((DatabaseKey) key);
				}
				finally {
					lock.unlockRead(stamp);
				}
			}
		}

		if (entry != null && entry.getKey().equals(key) && !entry.getKey().isExpired()) {
			return entry.getValue();
		}
		return null;
	}

	@Override
	public DatabaseValue put(DatabaseKey key, DatabaseValue value) {
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
	public void putAll(Map<? extends DatabaseKey, ? extends DatabaseValue> m) {
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
	public Set<DatabaseKey> keySet() {
		long stamp = lock.readLock();
		try {
			return unmodifiableSet(cache.keySet().stream().collect(toSet()));
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public Collection<DatabaseValue> values() {
		Collection<DatabaseValue> values = null;
		long stamp = lock.readLock();
		try {
			values = unmodifiableList(cache.values().stream().collect(toList()));
		}
		finally {
			lock.unlockRead(stamp);
		}
		return values;
	}

	@Override
	public Set<Entry<DatabaseKey, DatabaseValue>> entrySet() {
		long stamp = lock.readLock();
		try {
			return cache.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue())).collect(toSet());
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public DatabaseValue putIfAbsent(DatabaseKey key, DatabaseValue value) {
		long stamp = lock.writeLock();
		try {
			return cache.putIfAbsent(key, value);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public DatabaseValue merge(DatabaseKey key, DatabaseValue value, BiFunction<? super DatabaseValue, ? super DatabaseValue, ? extends DatabaseValue> remappingFunction) {
		long stamp = lock.writeLock();
		try {
			return cache.merge(key, value, remappingFunction);
		}
		finally {
			lock.unlockWrite(stamp);
		}
	}

	@Override
	public boolean isType(DatabaseKey key, DataType type) {
		long stamp = lock.readLock();
		try {
			return cache.getOrDefault(key, new DatabaseValue(type)).getType() == type;
		}
		finally {
			lock.unlockRead(stamp);
		}
	}

	@Override
	public boolean rename(DatabaseKey from, DatabaseKey to) {
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

package com.mawen.learn.redis.basic.data;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/17
 */
public class SimpleDatabase implements IDatabase {

	private final NavigableMap<DatabaseKey, DatabaseValue> cache;

	public SimpleDatabase() {
		this(new TreeMap<>());
	}

	public SimpleDatabase(NavigableMap<DatabaseKey, DatabaseValue> cache) {
		this.cache = cache;
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	@Override
	public DatabaseValue get(Object key) {
		Entry<DatabaseKey, DatabaseValue> entry = null;

		if (key instanceof DatabaseKey) {
			entry = getEntry((DatabaseKey) key);
		}

		return entry != null && !entry.getKey().isExpired() ? entry.getValue() : null;
	}

	private Entry<DatabaseKey, DatabaseValue> getEntry(DatabaseKey key) {
		Entry<DatabaseKey, DatabaseValue> entry = cache.ceilingEntry(key);
		return entry != null && entry.getKey().equals(key) ? entry : null;
	}

	@Override
	public DatabaseValue put(DatabaseKey key, DatabaseValue value) {
		return cache.put(key, value);
	}

	@Override
	public DatabaseValue remove(Object key) {
		return cache.remove(key);
	}

	@Override
	public void putAll(Map<? extends DatabaseKey, ? extends DatabaseValue> m) {
		cache.putAll(m);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Set<DatabaseKey> keySet() {
		return Collections.unmodifiableSet(cache.keySet().stream().collect(toSet()));
	}

	@Override
	public Collection<DatabaseValue> values() {
		return Collections.unmodifiableList(cache.values().stream().collect(toList()));
	}

	@Override
	public Set<Entry<DatabaseKey, DatabaseValue>> entrySet() {
		return cache.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue())).collect(toSet());
	}

	@Override
	public DatabaseValue putIfAbsent(DatabaseKey key, DatabaseValue value) {
		return cache.putIfAbsent(key, value);
	}

	@Override
	public DatabaseValue merge(DatabaseKey key, DatabaseValue value, BiFunction<? super DatabaseValue, ? super DatabaseValue, ? extends DatabaseValue> remappingFunction) {
		return cache.merge(key, value, remappingFunction);
	}

	@Override
	public boolean isType(DatabaseKey key, DataType type) {
		return cache.getOrDefault(key, new DatabaseValue(type)).getType() == type;
	}

	@Override
	public boolean rename(DatabaseKey from, DatabaseKey to) {
		DatabaseValue value = cache.remove(from);
		if (value != null) {
			cache.put(to, value);
			return true;
		}
		return false;
	}

	@Override
	public DatabaseKey overrideKey(DatabaseKey key) {
		Entry<DatabaseKey, DatabaseValue> entry = getEntry(key);

		if (entry != null) {
			cache.remove(key);
			cache.put(key, entry.getValue());
		}

		return entry != null ? entry.getKey() : null;
	}

	@Override
	public DatabaseKey getKey(DatabaseKey key) {
		Entry<DatabaseKey, DatabaseValue> entry = getEntry(key);

		return entry != null ? entry.getKey() : null;
	}
}

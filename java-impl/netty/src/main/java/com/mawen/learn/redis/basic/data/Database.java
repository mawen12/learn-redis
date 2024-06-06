package com.mawen.learn.redis.basic.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Database implements IDatabase {

	private final Map<String, DatabaseValue> cache = Collections.synchronizedMap(new HashMap<>());

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
		return cache.get(key);
	}

	@Override
	public DatabaseValue put(String key, DatabaseValue value) {
		return cache.put(key, value);
	}

	@Override
	public DatabaseValue remove(Object key) {
		return cache.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends DatabaseValue> m) {
		cache.putAll(m);
	}

	@Override
	public void clear() {
		cache.clear();
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
		return cache.putIfAbsent(key, value);
	}

	@Override
	public DatabaseValue merge(String key, DatabaseValue value, BiFunction<? super DatabaseValue, ? super DatabaseValue, ? extends DatabaseValue> remappingFunction) {
		return cache.merge(key, value, remappingFunction);
	}
}

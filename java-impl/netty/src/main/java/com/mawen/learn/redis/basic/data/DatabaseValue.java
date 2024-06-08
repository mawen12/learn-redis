package com.mawen.learn.redis.basic.data;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DatabaseValue {

	private final DataType type;

	private Object value;

	public DatabaseValue(DataType type) {
		this(type, null);
	}

	public DatabaseValue(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public <T> T getValue() {
		return (T) value;
	}

	public int incrementAndGet(int increment) throws NumberFormatException {
		int i = Integer.parseInt(value.toString()) + increment;
		this.value = String.valueOf(i);
		return i;
	}

	public int decrementAndGet(int decrement) throws NumberFormatException {
		int i = Integer.parseInt(value.toString()) - decrement;
		this.value = String.valueOf(i);
		return i;
	}

	public void append(String string) {
		this.value = this.value + string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DatabaseValue other = (DatabaseValue) obj;
		if (type != other.type) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		}
		else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public static DatabaseValue string(String value) {
		return new DatabaseValue(DataType.STRING, value);
	}

	public static DatabaseValue list(Collection<String> values) {
		return new DatabaseValue(DataType.LIST,
				values.stream().collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>()))));
	}

	public static DatabaseValue list(String... values) {
		return new DatabaseValue(DataType.LIST,
				Stream.of(values).collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>()))));
	}

	public static DatabaseValue set(Collection<String> values) {
		return new DatabaseValue(DataType.SET,
				values.stream().collect(Collectors.toCollection(() -> Collections.synchronizedSet(new LinkedHashSet<>()))));
	}

	public static DatabaseValue set(String... values) {
		return new DatabaseValue(DataType.SET,
				Stream.of(values).collect(Collectors.toCollection(() -> Collections.synchronizedSet(new LinkedHashSet<>()))));
	}

	public static DatabaseValue zset(Collection<String> values) {
		return new DatabaseValue(DataType.ZSET,
				values.stream().collect(Collectors.toCollection(() -> Collections.synchronizedSet(new TreeSet<>()))));
	}

	public static DatabaseValue zset(String... values) {
		return new DatabaseValue(DataType.ZSET,
				Stream.of(values).collect(Collectors.toCollection(() -> Collections.synchronizedSet(new TreeSet<>()))));
	}

	public static DatabaseValue hash(Collection<Map.Entry<String, String>> values) {
		return new DatabaseValue(DataType.HASH,
				values.stream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public static DatabaseValue hash(Map.Entry<String, String>... values) {
		return new DatabaseValue(DataType.HASH,
				Stream.of(values).collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public static Map.Entry<String, String> entry(String key, String value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
}

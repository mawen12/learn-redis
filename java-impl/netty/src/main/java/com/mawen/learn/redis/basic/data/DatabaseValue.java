package com.mawen.learn.redis.basic.data;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Collections.*;
import static java.util.Map.*;
import static java.util.stream.Collectors.*;
import static tonivade.equalizer.Equalizer.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DatabaseValue {

	public static final DatabaseValue EMPTY_STRING = string("");
	public static final DatabaseValue EMPTY_LIST = list();
	public static final DatabaseValue EMPTY_SET = set();
	public static final DatabaseValue EMPTY_ZSET = zset();
	public static final DatabaseValue EMPTY_HASH = hash();

	private final DataType type;

	private final Object value;

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

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		return equalizer(this)
				.append((one, other) -> Objects.equals(one.type, other.type))
				.append((one, other) -> Objects.equals(one.value, other.getValue()))
				.applyTo(obj);
	}

	@Override
	public String toString() {
		return "DatabaseValue [type=" + type + ", value=" + value + "]";
	}

	public static DatabaseValue string(String value) {
		return string(safeString(value));
	}

	public static DatabaseValue string(SafeString value) {
		return new DatabaseValue(DataType.STRING, value);
	}

	public static DatabaseValue list(Collection<SafeString> values) {
		return new DatabaseValue(DataType.LIST, unmodifiableList(values.stream().collect(toList())));
	}

	public static DatabaseValue list(SafeString... values) {
		return new DatabaseValue(DataType.LIST, unmodifiableList(Stream.of(values).collect(toList())));
	}

	public static DatabaseValue set(Collection<SafeString> values) {
		return new DatabaseValue(DataType.SET, unmodifiableSet(values.stream().collect(toSet())));
	}

	public static DatabaseValue set(SafeString... values) {
		return new DatabaseValue(DataType.SET, unmodifiableSet(Arrays.stream(values).collect(toSet())));
	}

	public static DatabaseValue zset(Collection<Entry<Double, SafeString>> values) {
		return new DatabaseValue(DataType.ZSET, unmodifiableNavigableSet(values.stream().collect(toSortedSet())));
	}

	public static DatabaseValue zset(Entry<Double, SafeString>... values) {
		return new DatabaseValue(DataType.ZSET, unmodifiableNavigableSet(Stream.of(values).collect(toSortedSet())));
	}

	public static DatabaseValue hash(Collection<Map.Entry<SafeString, SafeString>> values) {
		return new DatabaseValue(DataType.HASH, unmodifiableMap(values.stream().collect(toHash())));
	}

	public static DatabaseValue hash(Map.Entry<SafeString, SafeString>... values) {
		return new DatabaseValue(DataType.HASH, unmodifiableMap(Stream.of(values).collect(toHash())));
	}

	public static Map.Entry<SafeString, SafeString> entry(SafeString key, SafeString value) {
		return new SimpleEntry<>(key, value);
	}

	public static Entry<Double, SafeString> score(double score, SafeString value) {
		return new SimpleEntry<>(score, value);
	}

	private static Collector<SafeString, ?, LinkedList<SafeString>> toList() {
		return toCollection(LinkedList::new);
	}

	private static Collector<SafeString, ?, LinkedHashSet<SafeString>> toSet() {
		return toCollection(LinkedHashSet::new);
	}

	private static Collector<Entry<Double, SafeString>, ?, NavigableSet<Entry<Double, SafeString>>> toSortedSet() {
		return toCollection(SortedSet::new);
	}

	private static Collector<Entry<SafeString, SafeString>, ?, Map<SafeString, SafeString>> toHash() {
		return toMap(Entry::getKey, Entry::getValue);
	}
}

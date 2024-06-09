package com.mawen.learn.redis.basic.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
public class SortedSet implements NavigableSet<Entry<Float, String>> {

	private static final String EMPTY_STRING = "";

	private final Map<String, Float> items = new HashMap<>();

	private final NavigableSet<Entry<Float, String>> scores = new TreeSet<>(
			((o1, o2) -> {
				int key = o1.getKey().compareTo(o2.getKey());
				if (key != 0) {
					return key;
				}
				if (EMPTY_STRING.equals(o1.getValue())) {
					return 0;
				}
				if(EMPTY_STRING.equals(o2.getValue())) {
					return 0;
				}
				return o1.getValue().compareTo(o2.getValue());
			})
	);

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return items.containsKey(o);
	}

	@Override
	public Iterator<Entry<Float, String>> iterator() {
		return scores.iterator();
	}

	@Override
	public Object[] toArray() {
		return scores.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return scores.toArray(a);
	}

	@Override
	public boolean add(Entry<Float, String> e) {
		if (!items.containsKey(e.getValue())) {
			items.put(e.getValue(), e.getKey());
			scores.add(e);
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		if (items.containsKey(o)) {
			float score = items.remove(o);
			scores.remove(DatabaseValue.score(score, (String) o));
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean result = false;
		for (Object object : c) {
			result |= contains(object);
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends Entry<Float, String>> c) {
		boolean result = false;
		for (Entry<Float, String> entry : c) {
			result |= add(entry);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Set<String> toRemove = new HashSet<>(items.keySet());
		toRemove.removeAll(c);
		boolean result = false;

		for (String key : toRemove) {
			result |= remove(key);
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		for (Object object : c) {
			result |= remove(object);
		}
		return result;
	}

	@Override
	public void clear() {
		items.clear();
		scores.clear();
	}

	@Override
	public Entry<Float, String> lower(Entry<Float, String> e) {
		return scores.lower(e);
	}

	@Override
	public Entry<Float, String> floor(Entry<Float, String> e) {
		return scores.floor(e);
	}

	@Override
	public Entry<Float, String> ceiling(Entry<Float, String> e) {
		return scores.ceiling(e);
	}

	@Override
	public Entry<Float, String> higher(Entry<Float, String> e) {
		return scores.higher(e);
	}

	@Override
	public Entry<Float, String> pollFirst() {
		return scores.pollFirst();
	}

	@Override
	public Entry<Float, String> pollLast() {
		return scores.pollLast();
	}

	@Override
	public NavigableSet<Entry<Float, String>> descendingSet() {
		return scores.descendingSet();
	}

	@Override
	public Iterator<Entry<Float, String>> descendingIterator() {
		return scores.descendingIterator();
	}

	@Override
	public NavigableSet<Entry<Float, String>> subSet(Entry<Float, String> fromElement, boolean fromInclusive, Entry<Float, String> toElement, boolean toInclusive) {
		return scores.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}

	@Override
	public NavigableSet<Entry<Float, String>> headSet(Entry<Float, String> toElement, boolean inclusive) {
		return scores.headSet(toElement, inclusive);
	}

	@Override
	public NavigableSet<Entry<Float, String>> tailSet(Entry<Float, String> fromElement, boolean inclusive) {
		return scores.tailSet(fromElement, inclusive);
	}

	@Override
	public java.util.SortedSet<Entry<Float, String>> subSet(Entry<Float, String> fromElement, Entry<Float, String> toElement) {
		return scores.subSet(fromElement, toElement);
	}

	@Override
	public java.util.SortedSet<Entry<Float, String>> headSet(Entry<Float, String> toElement) {
		return scores.headSet(toElement);
	}

	@Override
	public java.util.SortedSet<Entry<Float, String>> tailSet(Entry<Float, String> fromElement) {
		return scores.tailSet(fromElement);
	}

	@Override
	public Comparator<? super Entry<Float, String>> comparator() {
		return scores.comparator();
	}

	@Override
	public Entry<Float, String> first() {
		return scores.first();
	}

	@Override
	public Entry<Float, String> last() {
		return scores.last();
	}

	public float score(String key) {
		if (items.containsKey(key)) {
			return items.get(key);
		}
		return Float.MIN_VALUE;
	}

	public int ranking(String key) {
		if (items.containsKey(key)) {
			float score = items.get(key);

			Set<Entry<Float, String>> head = scores.headSet(DatabaseValue.score(score, key));

			return head.size();
		}
		return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		if (obj instanceof Set) {
			Set<?> other = (Set<?>) obj;
			if (scores != null) {
				return scores.equals(other);
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return scores.toString();
	}
}

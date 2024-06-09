package com.mawen.learn.redis.basic.data;

import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SortedSetTest {

	@Test
	public void testName() {
		SortedSet set = new SortedSet();

		assertThat(set.add(score(1, "a")), is(true));
		assertThat(set.add(score(2, "a")), is(false));
		assertThat(set.add(score(2, "b")), is(true));

		assertThat(set.contains("a"), is(true));
		assertThat(set.contains("b"), is(true));
		assertThat(set.contains("c"), is(false));

		assertThat(set.score("a"), is(1.0F));
		assertThat(set.score("b"), is(2.0F));

		assertThat(set.ranking("a"), is(0));
		assertThat(set.ranking("b"), is(1));

		assertThat(set.remove("a"), is(true));
		assertThat(set.contains("a"), is(false));
	}

	@Test
	public void testEquals() {
		SortedSet setA = new SortedSet();

		setA.add(score(1, "a"));
		setA.add(score(2, "b"));

		SortedSet setB = new SortedSet();

		setB.add(score(1, "a"));
		setB.add(score(2, "b"));

		assertThat(setA, is(setB));
		assertThat(unmodifiableSet(setA), is(unmodifiableSet(setB)));
	}

	@Test
	public void testNotEquals() {
		SortedSet setA = new SortedSet();
		setA.add(score(1, "a"));

		SortedSet setB = new SortedSet();
		setB.add(score(1, "a"));
		setB.add(score(2, "b"));

		assertThat(setA, not(is(setB)));
	}

	@Test
	public void testScore() {
		SortedSet set = new SortedSet();
		set.add(score(1, "a"));
		set.add(score(2, "b"));
		set.add(score(3, "c"));
		set.add(score(4, "d"));
		set.add(score(5, "e"));
		set.add(score(6, "f"));
		set.add(score(7, "g"));
		set.add(score(8, "h"));
		set.add(score(9, "i"));

		assertThat(set.tailSet(score(3,"")).first(),is(score(3.0F,"c")));

		assertThat(set.headSet(score(4,"")).last(), is(score(3.0F,"c")));
	}
}
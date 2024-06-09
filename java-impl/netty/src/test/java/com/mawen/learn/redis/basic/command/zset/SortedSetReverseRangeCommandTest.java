package com.mawen.learn.redis.basic.command.zset;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SortedSetReverseRangeCommand.class)
public class SortedSetReverseRangeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.withData("key", zset(score(1, "a"), score(2, "b"), score(3, "c")))
				.withParams("key", "-1", "0")
				.execute()
				.verify().addArray(captor.capture());

		Collection<String> array = captor.getValue();

		assertThat(array.size(), is(3));

		Iterator<String> iter = array.iterator();

		assertThat(iter.next(), is("c"));
		assertThat(iter.next(), is("b"));
		assertThat(iter.next(), is("a"));
	}

}
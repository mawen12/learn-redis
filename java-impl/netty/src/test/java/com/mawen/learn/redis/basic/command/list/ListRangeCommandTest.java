package com.mawen.learn.redis.basic.command.list;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(ListRangeCommand.class)
public class ListRangeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "0", "-1")
				.execute()
				.verify().addArray(captor.capture());

		Collection<String> result = captor.getValue();

		assertThat(result.size(), is(3));

		Iterator<String> iter = result.iterator();

		assertThat(iter.next(), is("a"));
		assertThat(iter.next(), is("b"));
		assertThat(iter.next(), is("c"));
	}

}
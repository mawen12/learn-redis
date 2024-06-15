package com.mawen.learn.redis.basic.command.hash;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.entry;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(HashGetAllCommand.class)
public class HashGetAllCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<SafeString>> captor;

	@Test
	public void testExecute() {
		rule.withData("a", hash(entry("key1", "value1"), entry("key2", "value2"), entry("key3", "value3")))
				.withParams("a")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> value = captor.getValue();

		Iterator<SafeString> iter = value.iterator();

		assertThat(iter.next(), is(safeString("key1")));
		assertThat(iter.next(), is(safeString("value1")));
		assertThat(iter.next(), is(safeString("key3")));
		assertThat(iter.next(), is(safeString("value3")));
		assertThat(iter.next(), is(safeString("key2")));
		assertThat(iter.next(), is(safeString("value2")));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("a")
				.execute()
				.verify().addArray(anyCollectionOf(SafeString.class));
	}

}
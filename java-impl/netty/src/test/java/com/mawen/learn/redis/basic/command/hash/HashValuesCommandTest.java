package com.mawen.learn.redis.basic.command.hash;

import java.util.Collection;

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

@CommandUnderTest(HashValuesCommand.class)
public class HashValuesCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<SafeString>> captor;

	@Test
	public void testExecute() {
		rule.withData("test", hash(entry("key1", "value1"), entry("key2", "value2"), entry("key3", "value3")))
				.withParams("test")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> values = captor.getValue();

		assertThat(values.size(), is(3));
		assertThat(values.contains(safeString("value1")), is(true));
		assertThat(values.contains(safeString("value2")), is(true));
		assertThat(values.contains(safeString("value3")), is(true));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("test")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> values = captor.getValue();

		assertThat(values.isEmpty(), is(true));
	}
}
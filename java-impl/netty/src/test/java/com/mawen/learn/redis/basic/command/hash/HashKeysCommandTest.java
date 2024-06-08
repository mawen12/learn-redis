package com.mawen.learn.redis.basic.command.hash;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(HashKeysCommand.class)
public class HashKeysCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.withData("key",hash(entry("a","1"), entry("b","1")))
				.withParams("key", "a")
				.execute()
				.verify().addArray(captor.capture());

		Collection<String> keys = captor.getValue();

		assertThat(keys.size(), is(2));
		assertThat(keys.contains("a"), is(true));
		assertThat(keys.contains("b"), is(true));
	}

}
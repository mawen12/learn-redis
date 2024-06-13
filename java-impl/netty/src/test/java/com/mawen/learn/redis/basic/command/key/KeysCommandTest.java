package com.mawen.learn.redis.basic.command.key;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(KeysCommand.class)
public class KeysCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<?>> captor;

	@Test
	public void testExecute() {
		rule.withData("abc", string("1"))
				.withData("acd", string("2"))
				.withData("c", string("3"))
				.withParams("*")
				.execute()
				.verify().addArray(captor.capture());

		Collection<?> value = captor.getValue();

		assertThat(value.size(), is(3));
		assertThat(value.contains(safeString("abc")), is(true));
		assertThat(value.contains(safeString("acd")), is(true));
		assertThat(value.contains(safeString("c")), is(true));
	}

	@Test
	public void testExecuteExclamation() {
		rule.withData("abc", string("1"))
				.withData("acd", string("2"))
				.withData("c", string("3"))
				.withParams("a??")
				.execute()
				.verify().addArray(captor.capture());

		Collection<?> value = captor.getValue();

		assertThat(value.size(), is(2));
		assertThat(value.contains(safeString("abc")), is(true));
		assertThat(value.contains(safeString("acd")), is(true));
		assertThat(value.contains(safeString("c")), is(false));
	}

	@Test
	public void testExecuteAsterisk() {
		rule.withData("abc", string("1"))
				.withData("acd", string("2"))
				.withData("c", string("3"))
				.withParams("a*")
				.execute()
				.verify().addArray(captor.capture());

		Collection<?> value = captor.getValue();

		assertThat(value.size(), is(2));
		assertThat(value.contains(safeString("abc")), is(true));
		assertThat(value.contains(safeString("acd")), is(true));
		assertThat(value.contains(safeString("c")), is(false));
	}
}
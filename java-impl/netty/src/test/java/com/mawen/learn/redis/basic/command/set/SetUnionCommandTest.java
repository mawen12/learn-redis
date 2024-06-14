package com.mawen.learn.redis.basic.command.set;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.redis.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SetUnionCommand.class)
public class SetUnionCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<SafeString>> captor;

	@Test
	public void testExecute() {
		rule.withData("a", set("1", "2", "3"))
				.withData("b", set("3", "4"))
				.withParams("a", "b")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> result = captor.getValue();

		assertThat(result.size(), is(4));

		assertThat(result.contains(safeString("1")), is(true));
		assertThat(result.contains(safeString("2")), is(true));
		assertThat(result.contains(safeString("3")), is(true));
		assertThat(result.contains(safeString("4")), is(true));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withData("a", set("1", "2", "3"))
				.withParams("a", "b")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> result = captor.getValue();

		assertThat(result.size(), is(3));

		assertThat(result.contains(safeString("1")), is(true));
		assertThat(result.contains(safeString("2")), is(true));
		assertThat(result.contains(safeString("3")), is(true));
		assertThat(result.contains(safeString("4")), is(false));
	}
}
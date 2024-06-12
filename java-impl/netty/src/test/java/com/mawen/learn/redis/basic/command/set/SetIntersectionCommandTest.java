package com.mawen.learn.redis.basic.command.set;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SetIntersectionCommand.class)
public class SetIntersectionCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.withData("a", DatabaseValue.set("1", "2", "3"))
				.withData("b", DatabaseValue.set("3", "4"))
				.withParams("a", "b")
				.execute()
				.verify().addArray(captor.capture());

		Collection<String> result = captor.getValue();

		assertThat(result.size(),is(1));

		assertThat(result.contains("3"), is(true));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withData("a", DatabaseValue.set("1", "2", "3"))
				.withParams("a", "b")
				.execute()
				.verify().addArray(captor.capture());

		Collection<String> result = captor.getValue();

		assertThat(result.isEmpty(), is(true));
	}

}
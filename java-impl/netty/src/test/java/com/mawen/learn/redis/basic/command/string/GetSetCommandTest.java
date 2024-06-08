package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(GetSetCommand.class)
public class GetSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		rule.withData("a",string("1"))
				.withParams("a", "2")
				.execute()
				.verify().addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		assertThat(value.getValue(), is("1"));
	}

}
package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(GetCommand.class)
public class GetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<DatabaseValue> captor;

	@Test
	public void testExecute() {
		rule.withData("key",string("value"))
				.withParams("key")
				.execute()
				.verify().addValue(captor.capture());

		DatabaseValue value = captor.getValue();

		assertThat(value.getType(), is(DataType.STRING));
		assertThat(value.getValue(), is("value"));
	}

}
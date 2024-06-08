package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(FlushDBCommand.class)
public class FlushDBCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {

		rule.withData("a", string("test")).execute();

		assertThat(rule.getDatabase().isEmpty(), is(true));

		rule.verify().addSimpleStr("OK");
	}
}
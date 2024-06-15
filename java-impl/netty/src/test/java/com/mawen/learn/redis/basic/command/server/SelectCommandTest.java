package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SelectCommand.class)
public class SelectCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withParams("10")
				.execute();

		assertThat(rule.getSessionState().getCurrentDB(), is(10));
	}

	@Test
	public void testExecuteWithInvalidParam() {
		rule.withParams("asdasd")
				.execute()
				.verify().addError("ERR invalid DB index");
	}

}
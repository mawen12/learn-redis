package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(RenameCommand.class)
public class RenameCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a",string("1"))
				.withParams("a", "b")
				.execute()
				.assertValue("a",is(nullValue()))
				.assertValue("b",is(string("1")))
				.verify().addSimpleStr("OK");
	}

	@Test
	public void testExecuteError() {
		rule.withParams("a", "b")
				.execute()
				.assertValue("a",is(nullValue()))
				.assertValue("b",is(nullValue()))
				.verify().addError("ERR no such key");
	}

}
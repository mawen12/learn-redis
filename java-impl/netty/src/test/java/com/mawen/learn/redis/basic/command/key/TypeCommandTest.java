package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(TypeCommand.class)
public class TypeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a",string("test"))
				.withParams("a")
				.execute()
				.verify().addSimpleStr("string");
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("a")
				.execute()
				.verify().addSimpleStr("none");
	}

}
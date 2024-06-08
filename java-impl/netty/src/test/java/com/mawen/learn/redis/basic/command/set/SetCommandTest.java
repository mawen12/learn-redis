package com.mawen.learn.redis.basic.command.set;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import com.mawen.learn.redis.basic.command.string.SetCommand;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SetCommand.class)
public class SetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("a", "1")
				.execute()
				.assertThat("a",is(string("1")))
				.verify().addSimpleStr("OK");
	}
}
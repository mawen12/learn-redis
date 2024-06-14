package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(TimeToLiveCommand.class)
public class TimeToLiveCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("test", string("value"))
				.withParams("test")
				.execute()
				.verify().addInt(0L);
	}

}
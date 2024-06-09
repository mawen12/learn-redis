package com.mawen.learn.redis.basic.command.hash;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(HashSetCommand.class)
public class HashSetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a",hash(entry("key","value")))
				.withParams("a", "key", "value")
				.execute()
				.verify().addInt(false);
	}
}
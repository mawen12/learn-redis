package com.mawen.learn.redis.basic.command.hash;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(HashDeleteCommand.class)
public class HashDeleteCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key",hash(entry("a","1")))
				.withParams("key", "a", "b", "c")
				.execute()
				.verify().addInt(true);
	}
  
}
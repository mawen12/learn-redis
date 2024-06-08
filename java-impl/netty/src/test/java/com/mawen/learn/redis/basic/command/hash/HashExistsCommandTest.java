package com.mawen.learn.redis.basic.command.hash;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(HashExistsCommand.class)
public class HashExistsCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("key",hash(entry("a","1")))
				.withParams("key", "a")
				.execute()
				.verify().addInt(true);
	}
}
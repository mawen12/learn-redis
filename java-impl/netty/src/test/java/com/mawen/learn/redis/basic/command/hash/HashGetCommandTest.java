package com.mawen.learn.redis.basic.command.hash;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;

@CommandUnderTest(HashGetCommand.class)
public class HashGetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.withData("a",hash(entry("key", "value")))
				.withParams("a", "key")
				.execute()
				.verify().addBulkStr(safeString("value"));
	}
}
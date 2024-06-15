package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;

@CommandUnderTest(LeftPopCommand.class)
public class LeftPopCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", list("a", "b", "c"))
				.withParams("key")
				.execute()
				.assertValue("key", isList("b", "c"))
				.verify().addBulkStr(safeString("a"));
	}

}
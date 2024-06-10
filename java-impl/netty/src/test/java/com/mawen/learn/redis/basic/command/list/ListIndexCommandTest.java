package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(ListIndexCommand.class)
public class ListIndexCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "0")
				.execute()
				.verify().addBulkStr("a");

		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "-1")
				.execute()
				.verify().addBulkStr("c");

		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "-4")
				.execute()
				.verify().addBulkStr(null);

		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "4")
				.execute()
				.verify().addBulkStr(null);

		rule.withData("key", DatabaseValue.list("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.verify().addError(startsWith("ERR"));
	}

}
package com.mawen.learn.redis.basic.command.list;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(ListIndexCommand.class)
public class ListIndexCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "0")
				.execute()
				.verify().addBulkStr(safeString("a"));

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "-1")
				.execute()
				.verify().addBulkStr(safeString("c"));

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "-4")
				.execute()
				.verify().addBulkStr(null);

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "4")
				.execute()
				.verify().addBulkStr(null);

		rule.withData("key", list("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.verify().addError(startsWith("ERR"));
	}

}
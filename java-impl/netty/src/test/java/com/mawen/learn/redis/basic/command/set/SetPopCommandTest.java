package com.mawen.learn.redis.basic.command.set;

import java.util.Set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseKeyMatchers.*;
import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(SetPopCommand.class)
public class SetPopCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addBulkStr(notNull(SafeString.class));

		DatabaseValue value = rule.getDatabase().get(safeKey("key"));
		assertThat(value.<Set<String>>getValue().size(), is(2));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("key")
				.execute()
				.verify().addBulkStr(isNull(SafeString.class));
	}

}
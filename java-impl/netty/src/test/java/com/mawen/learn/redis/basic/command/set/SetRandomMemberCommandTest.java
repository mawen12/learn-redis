package com.mawen.learn.redis.basic.command.set;

import java.util.Set;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@CommandUnderTest(SetRandomMemberCommand.class)
public class SetRandomMemberCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", DatabaseValue.set("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addBulkStr(notNull(String.class));

		DatabaseValue value = rule.getDatabase().get("key");
		assertThat(value.<Set<String>>getValue().size(), is(3));
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("key")
				.execute()
				.verify().addBulkStr(isNull(String.class));
	}

}
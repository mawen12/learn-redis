package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.junit.Assert.*;

@CommandUnderTest(SetIsMemberCommand.class)
public class SetIsMemberCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key", "a")
				.execute()
				.verify().addInt(true);

		rule.withParams("key", "d")
				.execute()
				.verify().addInt(false);
	}

}
package com.mawen.learn.redis.basic.command.impl;

import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(RightPopCommand.class)
public class RightPopCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", list("a", "b", "c"))
				.withParams("key")
				.execute()
				.assertThat("key",is(list("a", "b")))
				.verify().addBulkStr("c");
	}

}
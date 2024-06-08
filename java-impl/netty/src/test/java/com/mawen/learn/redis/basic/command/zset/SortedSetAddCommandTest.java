package com.mawen.learn.redis.basic.command.zset;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SortedSetAddCommand.class)
public class SortedSetAddCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("key", "1", "one")
				.execute()
				.assertThat("key", is(zset(score(1.0F, "one"))))
				.verify().addInt(1);

		rule.withParams("key", "2", "two")
				.execute()
				.assertThat("key", is(zset(score(1.0F, "one"), score(2.0F, "two"))))
				.verify().addInt(1);

		rule.withParams("key", "1", "one")
				.execute()
				.assertThat("key", is(zset(score(1.0F, "one"), score(2.0F, "two"))))
				.verify().addInt(0);
	}

}
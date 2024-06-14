package com.mawen.learn.redis.basic.command.zset;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.score;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SortedSetAddCommand.class)
public class SortedSetAddCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withParams("key", "1", "one")
				.execute()
				.assertValue("key", is(zset(score(1.0, "one"))))
				.verify().addInt(1);

		rule.withParams("key", "2", "two")
				.execute()
				.assertValue("key", is(zset(score(1.0, "one"), score(2.0, "two"))))
				.verify().addInt(1);

		rule.withParams("key", "1", "one")
				.execute()
				.assertValue("key", is(zset(score(1.0, "one"), score(2.0, "two"))))
				.verify().addInt(0);
	}

}
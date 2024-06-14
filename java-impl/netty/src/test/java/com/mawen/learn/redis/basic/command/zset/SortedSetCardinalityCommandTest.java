package com.mawen.learn.redis.basic.command.zset;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.score;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

@CommandUnderTest(SortedSetCardinalityCommand.class)
public class SortedSetCardinalityCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", zset(score(1.0, "a"), score(2.0, "b"), score(3.0, "c")))
				.withParams("key")
				.execute()
				.verify().addInt(3);

		rule.withParams("notExists")
				.execute()
				.verify().addInt(0);
	}

}
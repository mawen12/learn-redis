package com.mawen.learn.redis.basic.command.zset;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;

@CommandUnderTest(SortedSetRemoveCommand.class)
public class SortedSetRemoveCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("key", zset(score(1.0F, "a"), score(2.0F, "b"), score(3.0F,"c")))
				.withParams("key", "a")
				.execute()
				.assertThat("key", is(zset(score(2.0F, "b"), score(3.0F, "c"))))
				.verify().addInt(1);

		rule.withParams("key", "a")
				.execute()
				.assertThat("key", is(zset(score(2.0F, "b"), score(3.0F, "c"))))
				.verify().addInt(0);
	}

}
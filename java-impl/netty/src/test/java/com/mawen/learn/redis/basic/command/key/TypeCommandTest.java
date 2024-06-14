package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.DatabaseValueMatchers;
import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import org.junit.Rule;
import org.junit.Test;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.score;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;


@CommandUnderTest(TypeCommand.class)
public class TypeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("a", string("test"))
				.withParams("a")
				.execute()
				.verify().addSimpleStr("string");
	}

	@Test
	public void testExecuteList() {
		rule.withData("a", DatabaseValueMatchers.list("a", "b", "c"))
				.withParams("a")
				.execute()
				.verify().addSimpleStr("list");
	}

	@Test
	public void testExecuteSet() {
		rule.withData("a", DatabaseValueMatchers.set("a", "b", "c"))
				.withParams("a")
				.execute()
				.verify().addSimpleStr("set");
	}

	@Test
	public void testExecuteZSet() {
		rule.withData("a", zset(score(1.0, "a"), score(2.0, "b"), score(3.0, "c")))
				.withParams("a")
				.execute()
				.verify().addSimpleStr("zset");
	}

	@Test
	public void testExecuteNotExists() {
		rule.withParams("a")
				.execute()
				.verify().addSimpleStr("none");
	}

}
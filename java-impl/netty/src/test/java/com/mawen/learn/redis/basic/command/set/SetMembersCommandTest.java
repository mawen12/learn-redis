package com.mawen.learn.redis.basic.command.set;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.DatabaseValueMatchers.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(SetMembersCommand.class)
public class SetMembersCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<SafeString>> captor;

	@Test
	public void testExecute() {
		rule.withData("key", set("a", "b", "c"))
				.withParams("key")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> value = captor.getValue();

		assertThat(value,contains(safeString("a"), safeString("b"), safeString("c")));
	}

}
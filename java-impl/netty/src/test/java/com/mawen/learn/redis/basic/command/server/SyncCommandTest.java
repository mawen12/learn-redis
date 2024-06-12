package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.command.IServerContext;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Matchers.*;

@CommandUnderTest(SyncCommand.class)
public class SyncCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() throws Exception {
		rule.execute()
				.verify(IServerContext.class).exportRDB(any());
	}

}
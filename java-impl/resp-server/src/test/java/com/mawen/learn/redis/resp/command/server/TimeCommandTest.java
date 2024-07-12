package com.mawen.learn.redis.resp.command.server;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.resp.command.CommandRule;
import com.mawen.learn.redis.resp.command.CommandUnderTest;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

@CommandUnderTest(TimeCommand.class)
public class TimeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<RedisToken>> captor;

	@Test
	public void testExecute() {
		rule.execute().verify().addArray(captor.capture());

		Collection<RedisToken> value = captor.getValue();

		Iterator<RedisToken> iterator = value.iterator();
		RedisToken secs = iterator.next();
		RedisToken mics = iterator.next();

		System.out.println(secs);
		System.out.println(mics);
	}

}
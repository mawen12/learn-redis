package com.mawen.learn.redis.basic.command.pubsub;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.command.ISession;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(UnsubscribeCommand.class)
public class UnsubscribeCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<String>> captor;

	@Test
	public void testExecute() {
		rule.withData("subscriptions:test", set("localhost:12345"))
				.withParams("test")
				.execute()
				.assertThat("subscriptions:test", is(set()));

		rule.verify(ISession.class).removeSubscription(safeString("test"));

		rule.verify().addArray(captor.capture());

		Collection<?> response = captor.getValue();

		assertThat(response.size(), is(3));

		Iterator<?> iter = response.iterator();

		assertThat(iter.next(), is(safeString("unsubscribe")));
		assertThat(iter.next(), is(safeString("test")));
		assertThat(iter.next(), is(0));
	}

}
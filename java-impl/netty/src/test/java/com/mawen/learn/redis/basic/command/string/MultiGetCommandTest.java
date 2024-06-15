package com.mawen.learn.redis.basic.command.string;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(MultiGetCommand.class)
public class MultiGetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<SafeString>> captor;

	@Test
	public void testExecute() {
		rule.withData("a",string("1"))
				.withData("c",string("2"))
				.withParams("a", "b", "c")
				.execute()
				.verify().addArray(captor.capture());

		Collection<SafeString> result = captor.getValue();

		Iterator<SafeString> iterator = result.iterator();

		assertThat(iterator.next(), is(safeString("1")));
		assertThat(iterator.next(), is(nullValue()));
		assertThat(iterator.next(), is(safeString("2")));
	}

}
package com.mawen.learn.redis.basic.command.string;

import java.util.Collection;
import java.util.Iterator;

import com.mawen.learn.redis.basic.command.impl.CommandRule;
import com.mawen.learn.redis.basic.command.impl.CommandUnderTest;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@CommandUnderTest(MultiGetCommand.class)
public class MultiGetCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<DatabaseValue>> captor;

	@Test
	public void testExecute() {
		rule.withData("a",string("1"))
				.withData("c",string("2"))
				.withParams("a", "b", "c")
				.execute()
				.verify().addArrayValue(captor.capture());

		Collection<DatabaseValue> result = captor.getValue();

		Iterator<DatabaseValue> iterator = result.iterator();
		DatabaseValue a = iterator.next();
		DatabaseValue b = iterator.next();
		DatabaseValue c = iterator.next();

		assertThat(a.getValue(), is("1"));
		assertThat(b, is(nullValue()));
		assertThat(c.getValue(), is("2"));
	}

}
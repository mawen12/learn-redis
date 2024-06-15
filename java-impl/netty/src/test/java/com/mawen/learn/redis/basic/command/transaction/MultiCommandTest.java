package com.mawen.learn.redis.basic.command.transaction;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.TransactionState;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

@CommandUnderTest(MultiCommand.class)
public class MultiCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void executeWithoutActiveTransaction() {
		rule.execute()
				.verify().addSimpleStr("OK");
	}

	@Test
	public void executeWithActiveTransaction() {
		when(rule.getSession().getValue("tx")).thenReturn(new TransactionState());

		rule.execute()
				.verify().addError("ERR MULTI calls can not be nested");
	}

}
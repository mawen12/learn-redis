package com.mawen.learn.redis.basic.command.transaction;

import java.util.Collection;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.TransactionState;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.Request;
import com.mawen.learn.redis.resp.protocol.SafeString;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@CommandUnderTest(ExecCommand.class)
public class ExecCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Captor
	private ArgumentCaptor<Collection<?>> captor;

	private final ICommand command = mock(ICommand.class);

	@Test
	public void executeWithActiveTransaction() {
		givenPingCommand();
		givenExistingTransaction();

		rule.execute().verify().addArray(captor.capture());

		verify(command, times(3)).execute(any(), any());

		Collection<?> value = captor.getValue();

		assertThat(value, hasSize(3));
	}

	@Test
	public void executeWithoutActiveTransaction() {
		rule.execute()
				.verify().addError("ERR EXEC without MULTI");
	}

	private void givenPingCommand() {
		when(rule.getServer().getCommand("ping")).thenReturn(command);
	}

	private void givenExistingTransaction() {
		TransactionState transaction = createTransaction();
		TransactionState noTransaction = null;

		when(rule.getSession().getValue("tx")).thenReturn(transaction, noTransaction);
		when(rule.getSession().removeValue("tx")).thenReturn(transaction);
	}

	private TransactionState createTransaction() {
		TransactionState transaction = new TransactionState();
		transaction.enqueue(new Request(null,null, SafeString.safeString("ping"), null));
		transaction.enqueue(new Request(null,null, SafeString.safeString("ping"), null));
		transaction.enqueue(new Request(null,null, SafeString.safeString("ping"), null));
		return transaction;
	}

}
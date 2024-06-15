package com.mawen.learn.redis.basic.command.transaction;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.TransactionState;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.TxIgnore;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.ISession;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
@Command("multi")
@TxIgnore
public class MultiCommand implements ITinyDBCommand {

	private static final String TRANSACTION_KEY = "tx";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (!isTxActive(request.getSession())) {
			createTransaction(request.getSession());
			response.addSimpleStr(IResponse.RESULT_OK);
		}
		else {
			response.addError("ERR MULTI calls can not be nested");
		}
	}

	private void createTransaction(ISession session) {
		session.putValue(TRANSACTION_KEY, new TransactionState());
	}

	private boolean isTxActive(ISession session) {
		return session.getValue(TRANSACTION_KEY) != null;
	}
}

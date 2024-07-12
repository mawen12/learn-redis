package com.mawen.learn.redis.basic.command.transaction;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.TransactionState;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.TxIgnore;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.ISession;
import com.mawen.learn.redis.resp.command.Response;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
@Command("exec")
@TxIgnore
public class ExecCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		TransactionState transaction = getTransactionIfExists(request.getSession());
		if (transaction != null) {
			ITinyDB server = getTinyDB(request.getServerContext());
			MetaResponse metaResponse = new MetaResponse();

			for (IRequest queuedRequest : transaction) {
				metaResponse.addResponse(executeCommand(server, queuedRequest));
			}

			response.addArray(metaResponse.build());
		}
		else {
			response.addError("ERR EXEC without MULTI");
		}
	}

	private Response executeCommand(ITinyDB server, IRequest queuedRequest) {
		Response response = new Response();
		ICommand command = server.getCommand(queuedRequest.getCommand());
		command.execute(queuedRequest, response);
		return response;
	}

	private TransactionState getTransactionIfExists(ISession session) {
		return session.removeValue("tx");
	}
}

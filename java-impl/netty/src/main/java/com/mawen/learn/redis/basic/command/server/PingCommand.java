package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.PubSubAllowed;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.redis.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@ReadOnly
@Command("ping")
@PubSubAllowed
public class PingCommand implements ICommand {

	private static final String PONG = "PONG";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (request.getLength() > 0) {
			response.addBulkStr(safeString(request.getParam(0)));
		}
		else {
			response.addSimpleStr(PONG);
		}
	}
}

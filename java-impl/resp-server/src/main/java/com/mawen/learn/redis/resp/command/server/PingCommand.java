package com.mawen.learn.redis.resp.command.server;


import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("ping")
public class PingCommand implements ICommand {

	private static final String PONG = "PONG";

	@Override
	public void execute(IRequest request, IResponse response) {
		if (request.getLength() > 0) {
			response.addBulkStr(request.getParam(0));
		}
		else {
			response.addSimpleStr(PONG);
		}
	}
}

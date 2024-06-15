package com.mawen.learn.redis.resp.command;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class NullCommand implements ICommand {

	@Override
	public void execute(IRequest request, IResponse response) {
		response.addError("ERR unknown command '" + request.getCommand() + "'");
	}
}

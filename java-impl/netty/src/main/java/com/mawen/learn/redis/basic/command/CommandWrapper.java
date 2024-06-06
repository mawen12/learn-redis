package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class CommandWrapper implements ICommand {

	private final ICommand command;
	private final int params;

	public CommandWrapper(ICommand command, int params) {
		this.params = params;
		this.command = command;
	}

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		if (request.getLength() < params) {
			response.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
		}
		else {
			command.execute(db, request, response);
		}
	}
}

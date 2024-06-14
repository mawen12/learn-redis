package com.mawen.learn.redis.resp.command;

import com.mawen.learn.redis.resp.annotation.ParamLength;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class CommandWrapper implements ICommand {

	private int params;

	private final ICommand command;

	public CommandWrapper(ICommand command) {
		this.command = command;
		ParamLength length = command.getClass().getAnnotation(ParamLength.class);
		if (length != null) {
			this.params = length.value();
		}
	}

	@Override
	public void execute(IRequest request, IResponse response) {
		if (request.getLength() < params) {
			response.addError("ERR wrong number of arguments for '" + request.getCommand() + "' command");
		}
		else {
			command.execute(request, response);
		}
	}
}

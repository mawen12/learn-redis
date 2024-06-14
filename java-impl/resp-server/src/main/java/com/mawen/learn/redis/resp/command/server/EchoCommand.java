package com.mawen.learn.redis.resp.command.server;


import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("echo")
@ParamLength(1)
public class EchoCommand implements ICommand {

	@Override
	public void execute(IRequest request, IResponse response) {
		response.addBulkStr(request.getParam(0));
	}
}

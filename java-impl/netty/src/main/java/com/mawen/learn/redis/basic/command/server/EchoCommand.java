package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.redis.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@ReadOnly
@Command("echo")
@ParamLength(1)
public class EchoCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		response.addBulkStr(safeString(request.getParam(0)));
	}
}

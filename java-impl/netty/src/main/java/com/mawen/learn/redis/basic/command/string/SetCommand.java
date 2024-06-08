package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("set")
@ParamLength(2)
@ParamType(DataType.STRING)
public class SetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		db.put(request.getParam(0), string(request.getParam(1)));
		response.addSimpleStr(RESULT_OK);
	}
}

package com.mawen.learn.redis.basic.command.string;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@ReadOnly
@Command("get")
@ParamLength(1)
@ParamType(DataType.STRING)
public class GetCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		response.addValue(db.get(safeKey(request.getParam(0))));
	}
}

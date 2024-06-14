package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
@Command("persist")
@ParamLength(1)
public class PersistCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseKey key = db.overrideKey(safeKey(request.getParam(0)));
		response.addInt(key != null);
	}
}

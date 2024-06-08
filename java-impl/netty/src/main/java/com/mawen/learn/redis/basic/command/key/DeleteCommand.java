package com.mawen.learn.redis.basic.command.key;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
@Command("del")
@ParamLength(1)
public class DeleteCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		int removed = 0;
		for (String key : request.getParams()) {
			DatabaseValue value = db.remove(key);
			if (value != null) {
				removed += 1;
			}
		}

		response.addInt(removed);
	}
}

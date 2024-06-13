package com.mawen.learn.redis.basic.command.set;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@ReadOnly
@Command("srandmember")
@ParamLength(1)
@ParamType(DataType.SET)
public class SetRandomMemberCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<String> random = new LinkedList<>();
		db.merge(safeKey(request.getParam(0)), EMPTY_SET, (oldValue, newValue) -> {
			List<String> merge = new ArrayList<>(oldValue.<Set<String>>getValue());
			random.add(merge.get(random(merge)));
			return set(merge);
		});

		if (random.isEmpty()) {
			response.addBulkStr(null);
		}
		else {
			response.addBulkStr(safeString(random.get(0)));
		}
	}

	private int random(List<String> merge) {
		return new Random().nextInt(merge.size());
	}
}

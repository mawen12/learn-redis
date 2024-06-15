package com.mawen.learn.redis.basic.command.set;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@Command("spop")
@ParamLength(1)
@ParamType(DataType.SET)
public class SetPopCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		List<SafeString> removed = new LinkedList<>();
		db.merge(safeKey(request.getParam(0)), EMPTY_SET, (oldValue, newValue) -> {
			List<SafeString> merge = new ArrayList<>(oldValue.<Set<SafeString>>getValue());
			removed.add(merge.remove(random(merge)));
			return set(merge);
		});

		if (removed.isEmpty()) {
			response.addBulkStr(null);
		}
		else {
			response.addBulkStr(removed.get(0));
		}
	}

	private int random(List<SafeString> merge) {
		return new Random().nextInt(merge.size());
	}
}

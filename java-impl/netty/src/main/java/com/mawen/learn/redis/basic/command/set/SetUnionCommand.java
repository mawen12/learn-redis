package com.mawen.learn.redis.basic.command.set;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/9
 */
@ReadOnly
@Command("sunion")
@ParamLength(2)
@ParamType(DataType.SET)
public class SetUnionCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue first = db.getOrDefault(safeKey(request.getParam(0)), DatabaseValue.EMPTY_SET);
		Set<SafeString> result = new HashSet<>(first.<Set<SafeString>>getValue());

		for (DatabaseKey param : request.getParams().stream().skip(1).map(DatabaseKey::safeKey).collect(Collectors.toList())) {
			result.addAll(db.getOrDefault(param, DatabaseValue.set()).<Set<SafeString>>getValue());
		}

		response.addArray(result);
	}
}

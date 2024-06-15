package com.mawen.learn.redis.basic.command.hash;

import java.util.Map;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ParamType;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseValue;
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
 * @since 2024/6/7
 */
@ReadOnly
@Command("hlen")
@ParamLength(1)
@ParamType(DataType.HASH)
public class HashLengthCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		DatabaseValue value = db.getOrDefault(safeKey(request.getParam(0)), EMPTY_HASH);

		Map<SafeString, SafeString> map = value.getValue();

		response.addInt(map.size());
	}
}

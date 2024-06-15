package com.mawen.learn.redis.basic.command.list;

import java.util.List;

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
 * @since 2024/6/10
 */
@ReadOnly
@Command("lindex")
@ParamLength(2)
@ParamType(DataType.LIST)
public class ListIndexCommand implements ITinyDBCommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			DatabaseValue value = db.getOrDefault(safeKey(request.getParam(0)), EMPTY_LIST);
			List<SafeString> list = value.getValue();

			int index = Integer.parseInt(request.getParam(1).toString());
			if (index < 0) {
				index += list.size();
			}

			response.addBulkStr(list.get(index));
		}
		catch (NumberFormatException e) {
			response.addError("ERR value is not an integer or out of range");
		}
		catch (IndexOutOfBoundsException e) {
			response.addBulkStr(null);
		}
	}
}

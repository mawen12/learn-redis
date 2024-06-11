package com.mawen.learn.redis.basic.command;

import java.util.Collection;

import com.mawen.learn.redis.basic.data.DatabaseValue;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IResponse {

	IResponse addValue(DatabaseValue value);

	IResponse addArrayValue(Collection<DatabaseValue> array);

	IResponse addArray(Collection<?> array);

	IResponse addBulkStr(String str);

	IResponse addSimpleStr(String str);

	IResponse addInt(String str);

	IResponse addInt(int value);

	IResponse addInt(boolean value);

	IResponse addError(String str);

	void exit();

	boolean isExit();
}

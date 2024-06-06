package com.mawen.learn.redis.basic.command;

import java.util.Collection;

import com.mawen.learn.redis.basic.data.DatabaseValue;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IResponse {

	IResponse addValue(DatabaseValue value);

	IResponse addArray(Collection<DatabaseValue> array);

	IResponse addBulkStr(Object str);

	IResponse addSimpleStr(Object str);

	IResponse addInt(Object str);

	IResponse addError(Object str);
}

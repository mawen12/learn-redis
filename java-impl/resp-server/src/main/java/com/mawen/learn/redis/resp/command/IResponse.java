package com.mawen.learn.redis.resp.command;

import java.util.Collection;

import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IResponse {

	String RESULT_OK = "OK";
	String RESULT_ERROR = "ERR";

	IResponse addArray(Collection<?> array);

	IResponse addBulkStr(SafeString str);

	IResponse addSimpleStr(String str);

	IResponse addInt(SafeString str);

	IResponse addInt(int value);

	IResponse addInt(long value);

	IResponse addInt(boolean value);

	IResponse addError(String str);

	void exit();

	boolean isExit();
}

package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.data.IDatabase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface ICommand {

	String OK = "OK";
	String ERROR = "ERR";

	void execute(IDatabase db, IRequest request, IResponse response);
}

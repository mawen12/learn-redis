package com.mawen.learn.redis.resp.command;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface ICommand {

	void execute(IRequest request, IResponse response);
}

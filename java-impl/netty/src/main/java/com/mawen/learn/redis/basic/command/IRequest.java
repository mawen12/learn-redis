package com.mawen.learn.redis.basic.command;

import java.util.List;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IRequest {

	String getCommand();

	List<String> getParams();

	String getParam(int i);

	int getLength();
}

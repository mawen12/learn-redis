package com.mawen.learn.redis.resp.command;

import java.util.List;
import java.util.Optional;

import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public interface IRequest {

	String getCommand();

	List<SafeString> getParams();

	SafeString getParam(int i);

	Optional<SafeString> getOptionalParam(int i);

	int getLength();

	ISession getSession();

	IServerContext getServerContext();
}

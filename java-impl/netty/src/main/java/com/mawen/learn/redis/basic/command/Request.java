package com.mawen.learn.redis.basic.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mawen.learn.redis.basic.redis.SafeString;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Request implements IRequest {

	private final SafeString command;

	private final List<SafeString> params;

	private ISession session;

	private IServerContext server;

	public Request(IServerContext server, ISession session, SafeString command, List<SafeString> params) {
		super();
		this.server = server;
		this.session = session;
		this.command = command;
		this.params = params;
	}

	@Override
	public String getCommand() {
		return command.toString();
	}

	@Override
	public List<String> getParams() {
		return params.stream().map(SafeString::toString).collect(toList());
	}

	@Override
	public List<SafeString> getSafeParams() {
		return Collections.unmodifiableList(params);
	}

	@Override
	public String getParam(int i) {
		if (i < params.size()) {
			return params.get(i).toString();
		}
		return null;
	}

	@Override
	public SafeString getSafeParam(int i) {
		if (i < params.size()) {
			return params.get(i);
		}
		return null;
	}

	@Override
	public Optional<String> getOptionalParam(int i) {
		return Optional.ofNullable(getParam(i));
	}

	@Override
	public int getLength() {
		return params.size();
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	public IServerContext getServerContext() {
		return server;
	}

	@Override
	public String toString() {
		return command + "[" + params.size() + "]: " + params;
	}
}

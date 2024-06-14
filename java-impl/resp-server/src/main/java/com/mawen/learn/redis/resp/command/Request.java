package com.mawen.learn.redis.resp.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Request implements IRequest {

	private final SafeString command;

	private final List<SafeString> params;

	private final ISession session;

	private final IServerContext server;

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
	public List<SafeString> getParams() {
		return Collections.unmodifiableList(params);
	}

	@Override
	public SafeString getParam(int i) {
		if (i < params.size()) {
			return params.get(i);
		}
		return null;
	}

	@Override
	public Optional<SafeString> getOptionalParam(int i) {
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

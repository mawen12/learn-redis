package com.mawen.learn.redis.basic.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Request implements IRequest {

	private final String command;

	private final List<String> params;

	public Request(String command, List<String> params) {
		super();
		this.command = command;
		this.params = params;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public List<String> getParams() {
		return Collections.unmodifiableList(params);
	}

	@Override
	public String getParam(int i) {
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
	public String toString() {
		return command + "[" + params.size() + "]: " + params;
	}
}

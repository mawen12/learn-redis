package com.mawen.learn.redis.basic.command;

import java.util.List;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Request implements IRequest {

	private String command;

	private List<String> params;

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return command;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public List<String> getParams() {
		return params;
	}

	@Override
	public String getParam(int i) {
		return params.get(i);
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

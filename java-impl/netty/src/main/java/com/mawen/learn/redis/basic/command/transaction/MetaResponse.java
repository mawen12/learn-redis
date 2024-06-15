package com.mawen.learn.redis.basic.command.transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.Response;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class MetaResponse {

	private final IResponse parent;

	private final List<Response> responses = new LinkedList<>();


	public MetaResponse(IResponse response) {
		this.parent = response;
	}

	public void addResponse(Response response) {
		responses.add(response);
	}

	public void build() {
		parent.addArray(responsesToArray());
	}

	private List<byte[]> responsesToArray() {
		return responses.stream().map(Response::getBytes).collect(toList());
	}

}

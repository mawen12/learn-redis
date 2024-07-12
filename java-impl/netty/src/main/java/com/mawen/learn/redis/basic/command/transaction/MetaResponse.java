package com.mawen.learn.redis.basic.command.transaction;

import java.util.LinkedList;
import java.util.List;

import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.Response;
import com.mawen.learn.redis.resp.protocol.RedisToken;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class MetaResponse {

	private final List<Response> responses = new LinkedList<>();


	public void addResponse(Response response) {
		responses.add(response);
	}

	public List<RedisToken> build() {
		return responses.stream().map(IResponse::build).collect(toList());
	}
}

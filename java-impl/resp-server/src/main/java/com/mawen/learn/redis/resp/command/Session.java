package com.mawen.learn.redis.resp.command;

import java.util.HashMap;
import java.util.Map;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public class Session implements ISession {

	private final String id;

	private final ChannelHandlerContext ctx;

	private final Map<String, Object> state = new HashMap<>();

	public Session(String id, ChannelHandlerContext ctx) {
		super();
		this.id = id;
		this.ctx = ctx;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void publish(RedisToken msg) {
		ctx.writeAndFlush(msg);
	}

	@Override
	public void close() {
		ctx.close();
	}

	@Override
	public void destroy() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		return (T) state.get(key);
	}

	@Override
	public void putValue(String key, Object value) {
		state.put(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T removeValue(String key) {
		return (T) state.remove(key);
	}
}

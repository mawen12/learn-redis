package com.mawen.learn.redis.basic.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public class Session implements ISession{

	private final String id;

	private final ChannelHandlerContext ctx;

	private int db;

	private final Set<String> subscriptions = new HashSet<>();

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
	public ChannelHandlerContext getContext() {
		return ctx;
	}

	@Override
	public Set<String> getSubscriptions() {
		return subscriptions;
	}

	@Override
	public void addSubscription(String channel) {
		subscriptions.add(channel);
	}

	@Override
	public void removeSubscription(String channel) {
		subscriptions.remove(channel);
	}

	@Override
	public int getCurrentDB() {
		return db;
	}

	@Override
	public void setCurrentDB(int db) {
		this.db = db;
	}
}

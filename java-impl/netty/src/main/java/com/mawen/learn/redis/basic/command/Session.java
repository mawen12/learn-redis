package com.mawen.learn.redis.basic.command;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mawen.learn.redis.basic.redis.SafeString;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public class Session implements ISession{

	private final String id;

	private final ChannelHandlerContext ctx;

	private int db;

	private final Set<SafeString> subscriptions = new HashSet<>();

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
	public Set<SafeString> getSubscriptions() {
		return subscriptions;
	}

	@Override
	public void addSubscription(SafeString channel) {
		subscriptions.add(channel);
	}

	@Override
	public void removeSubscription(SafeString channel) {
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

	@Override
	public void enqueue(Runnable task) {
		executor.submit(task);
	}

	@Override
	public void destroy() {
		executor.shutdown();
	}
}

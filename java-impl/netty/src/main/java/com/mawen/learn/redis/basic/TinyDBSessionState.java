package com.mawen.learn.redis.basic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class TinyDBSessionState {

	private int db;

	private final Set<SafeString> subscriptions = new HashSet<>();

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public int getCurrentDB() {
		return db;
	}

	public void setCurrentDB(int db) {
		this.db = db;
	}

	public Set<SafeString> getSubscriptions() {
		return subscriptions;
	}

	public void addSubscription(SafeString channel) {
		subscriptions.add(channel);
	}

	public void removeSubscription(SafeString channel) {
		subscriptions.remove(channel);
	}

	public void enqueue(Runnable task) {
		executor.execute(task);
	}

	public boolean isSubscribed() {
		return !subscriptions.isEmpty();
	}

	public void destroy() {
		executor.shutdown();
	}

}

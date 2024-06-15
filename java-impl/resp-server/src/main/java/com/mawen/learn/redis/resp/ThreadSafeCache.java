package com.mawen.learn.redis.resp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class ThreadSafeCache<K, V> {

	private static final Logger logger = Logger.getLogger(ThreadSafeCache.class.getName());

	private final Map<K, FutureTask<V>> values = new HashMap<>();

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public V get(K key) {
		try {
			return getValue(key).get();
		}
		catch (InterruptedException e) {
			logger.log(Level.WARNING,"interrupted",e);
		}
		catch (ExecutionException e) {
			logger.log(Level.SEVERE, "error", e);
		}
		return null;
	}

	private FutureTask<V> getValue(K key) {
		return values.getOrDefault(key, new FutureTask<>(() -> null));
	}

	public V get(K key, Function<K, V> objectCreator, Consumer<V> creationCallback) {
		try {
			FutureTask<V> task = getOrCreateTask(key, objectCreator, creationCallback);
			logger.log(Level.FINE, "getting object from cache `{0}`", key);
			return task.get();
		}
		catch (InterruptedException e) {
			logger.log(Level.WARNING, "interrupted", e);
		}
		catch (ExecutionException e) {
			logger.log(Level.SEVERE, "error", e);
		}
		return null;
	}

	private FutureTask<V> getOrCreateTask(K key, Function<K, V> objectCreator, Consumer<V> creationCallback) {
		FutureTask<V> task;
		synchronized (values) {
			task = values.get(key);
			if (task == null) {
				task = createTask(key, objectCreator, creationCallback);
				executor.execute(task);
				values.put(key, task);
			}
		}

		return task;
	}

	private FutureTask<V> createTask(K key, Function<K, V> objectCreator, Consumer<V> creationCallback) {
		return new FutureTask<>(() -> createObject(key,objectCreator, creationCallback));
	}

	private V createObject(K key, Function<K, V> objectCreator, Consumer<V> creationCallback) {
		logger.log(Level.INFO,"creating object from cache `{0}`", key);
		V createdObject = objectCreator.apply(key);
		if (createdObject != null) {
			creationCallback.accept(createdObject);
		}
		return createdObject;
	}

	public V remove(K key) {
		try {
			FutureTask<V> task;
			synchronized (values) {
				task = values.remove(key);
			}
			return task != null ? task.get() : null;
		}
		catch (InterruptedException e) {
			logger.log(Level.WARNING, "interrupted", e);
		}
		catch (ExecutionException e) {
			logger.log(Level.SEVERE, "error", e);
		}
		return null;
	}

	public int size() {
		synchronized (values) {
			return values.size();
		}
	}

	public void clear() {
		synchronized (values) {
			values.clear();
		}
	}

	public void destroy() {
		executor.shutdown();
	}
}

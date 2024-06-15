package com.mawen.learn.redis.basic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mawen.learn.redis.resp.command.IRequest;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class TransactionState implements Iterable<IRequest> {

	private final List<IRequest> requests = new LinkedList<>();

	public void enqueue(IRequest request) {
		requests.add(request);
	}

	public int size() {
		return requests.size();
	}

	@Override
	public Iterator<IRequest> iterator() {
		return requests.iterator();
	}
}

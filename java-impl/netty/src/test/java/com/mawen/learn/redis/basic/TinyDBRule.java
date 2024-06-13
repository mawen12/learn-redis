package com.mawen.learn.redis.basic;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.mawen.learn.redis.basic.TinyDBConfig.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class TinyDBRule implements TestRule {

	private final TinyDB server;

	public TinyDBRule() {
		this(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT);
	}

	public TinyDBRule(String host, int port) {
		this(host, port, withoutPersistence());
	}

	public TinyDBRule(String host, int port, TinyDBConfig config) {
		this.server = new TinyDB(host, port, config);
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					server.start();
					base.evaluate();
				}
				finally {
					server.stop();
				}
			}
		};
	}
}

package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.command.CommandSuite;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
public class RedisServerRule implements TestRule {

	private static final String DEFAULT_HOST = "localhost";

	private static final int DEFAULT_PORT = 12345;

	private final RedisServer server;

	public RedisServerRule() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public RedisServerRule(String host, int port) {
		this.server = new RedisServer(host, port, new CommandSuite());
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

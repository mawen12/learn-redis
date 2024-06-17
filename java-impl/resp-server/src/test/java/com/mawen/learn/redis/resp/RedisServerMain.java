package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.command.CommandSuite;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/17
 */
public class RedisServerMain {

	public static void main(String[] args) {
		RedisServer redisServer = new RedisServer("localhost", 12345, new CommandSuite());

		redisServer.start();
	}
}

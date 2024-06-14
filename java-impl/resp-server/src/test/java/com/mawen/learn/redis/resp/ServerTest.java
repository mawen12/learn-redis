package com.mawen.learn.redis.resp;

import com.mawen.learn.redis.resp.command.CommandSuite;

public class ServerTest {

	public static void main(String[] args) {
		RedisServer server = new RedisServer("localhost", 12345, new CommandSuite());

		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
	}

}
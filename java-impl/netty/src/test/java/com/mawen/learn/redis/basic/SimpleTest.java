package com.mawen.learn.redis.basic;

import redis.clients.jedis.Jedis;

public class SimpleTest extends Thread {

	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis("localhost", 7081);
		long nanos = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			jedis.set("key" + i, "value" + i);
		}

		System.out.println((System.currentTimeMillis() - nanos));
		jedis.close();
	}
}
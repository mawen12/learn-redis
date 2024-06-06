package com.mawen.learn.redis.basic;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		TinyDB db = new TinyDB();
		db.init();
		db.start();

		Runtime.getRuntime().addShutdownHook(new Thread(db::stop));
	}
}

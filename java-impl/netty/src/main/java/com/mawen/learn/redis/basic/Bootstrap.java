package com.mawen.learn.redis.basic;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		System.out.println("usage: Bootstrap <host> <port>");

		TinyDB db = new TinyDB(parseHost(args), parsePort(args));
		db.start();

		Runtime.getRuntime().addShutdownHook(new Thread(db::stop));
	}

	private static String parseHost(String[] args) {
		String host = TinyDB.DEFAULT_HOST;
		if (args.length > 0) {
			host = args[0];
		}
		return host;
	}

	private static int parsePort(String[] args) {
		int port = TinyDB.DEFAULT_PORT;
		if (args.length > 1) {
			try {
				port = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e) {
				System.out.println("wrong part value " + args[1]);
			}
		}
		return port;
	}
}

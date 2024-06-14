package com.mawen.learn.redis.basic;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;

import static com.mawen.learn.redis.basic.ITinyDB.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class Server {

	public static void main(String[] args) throws IOException {
		OptionParser parser = new OptionParser();
		OptionSpecBuilder help = parser.accepts("help", "print help");
		OptionSpecBuilder persist = parser.accepts("P", "with persistence");
		OptionSpec<String> host = parser.accepts("h", "host")
				.withRequiredArg().ofType(String.class).defaultsTo(TinyDB.DEFAULT_HOST);
		OptionSpec<Integer> port = parser.accepts("p", "port")
				.withRequiredArg().ofType(Integer.class).defaultsTo(TinyDB.DEFAULT_PORT);

		OptionSet options = parser.parse(args);

		if (options.has(help)) {
			parser.printHelpOn(System.out);
		}
		else {
			String optionHost = options.valueOf(host);
			int optionPort = parsePort(options.valueOf(port));
			TinyDBConfig optionPersistence = parseConfig(options.has(persist));
			TinyDB server = new TinyDB(optionHost, optionPort, optionPersistence);
			server.start();

			Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
		}

	}

	private static int parsePort(Integer optionPort) {
		return optionPort != null ? optionPort : DEFAULT_PORT;
	}

	private static TinyDBConfig parseConfig(boolean persist) {
		return persist ? TinyDBConfig.withPersistence() : TinyDBConfig.withoutPersistence();
	}
}

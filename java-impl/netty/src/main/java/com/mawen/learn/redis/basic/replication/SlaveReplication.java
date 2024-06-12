package com.mawen.learn.redis.basic.replication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.ITinyDBCallback;
import com.mawen.learn.redis.basic.TinyDBClient;
import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.ISession;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.persistence.ByteBufferInputStream;
import com.mawen.learn.redis.basic.redis.RedisArray;
import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class SlaveReplication implements ITinyDBCallback {

	private static final Logger logger = Logger.getLogger(SlaveReplication.class.getName());

	private final TinyDBClient client;

	private final IServerContext server;

	private final ISession session;

	public SlaveReplication(IServerContext server, ISession session, String host, int port) {
		this.server = server;
		this.session = session;
		this.client = new TinyDBClient(host, port, this);
	}

	public void start() {
		this.client.start();
	}

	public void stop() {
		this.client.stop();
	}

	@Override
	public void onConnect() {
		this.client.send("SYNC\r\n");
	}

	@Override
	public void onDisconnect() {

	}

	@Override
	public void onMessage(RedisToken token) {
		switch (token.getType()) {
			case STRING:
				processRDB(token);
				break;
			case ARRAY:
				processCommand(token);
				break;
			default:
				break;
		}
	}

	private void processCommand(RedisToken token) {
		RedisArray array = token.<RedisArray>getValue();

		RedisToken commandToken = array.remove(0);

		ICommand command = server.getCommand(commandToken.getValue().toString());

		if (command != null) {
			IDatabase current = server.getDatabase(session.getCurrentDB());
			command.execute(current, request(commandToken, array), new Response());
		}
	}

	private Request request(RedisToken commandToken, RedisArray array) {
		return new Request(server, session, commandToken.getValue(), toList(array));
	}

	private List<SafeString> toList(RedisArray request) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : request) {
			cmd.add(token.<SafeString>getValue());
		}
		return cmd;
	}

	private void processRDB(RedisToken token) {
		// RDB dump
		try {
			SafeString value = token.getValue();
			server.importRDB(array(value));
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "error importing RDB file", e);
		}
	}

	private InputStream array(SafeString value) throws UnsupportedEncodingException {
		return new ByteBufferInputStream(value.getBytes());
	}
}

package com.mawen.learn.redis.basic.replication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.persistence.ByteBufferInputStream;
import com.mawen.learn.redis.resp.IRedisCallback;
import com.mawen.learn.redis.resp.RedisClient;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.ISession;
import com.mawen.learn.redis.resp.command.Request;
import com.mawen.learn.redis.resp.command.Response;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class SlaveReplication implements IRedisCallback {

	private static final Logger logger = Logger.getLogger(SlaveReplication.class.getName());

	private final RedisClient client;

	private final ITinyDB server;

	private final ISession session;

	public SlaveReplication(ITinyDB server, ISession session, String host, int port) {
		this.server = server;
		this.session = session;
		this.client = new RedisClient(host, port, this);
	}

	public void start() {
		client.start();
		server.setMaster(false);
	}

	public void stop() {
		client.stop();
		server.setMaster(true);
	}

	@Override
	public void onConnect() {
		logger.info(() -> "Connected with master");

	}

	@Override
	public void onDisconnect() {
		logger.info(() -> "Disconnected from master");
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
		List<RedisToken> array = token.getValue();

		RedisToken commandToken = array.remove(0);

		logger.fine(() -> "command received from master: " + commandToken.getValue());

		ICommand command = server.getCommand(commandToken.getValue().toString());

		if (command != null) {
			command.execute(request(commandToken,array),new Response());
		}
	}

	private Request request(RedisToken commandToken, List<RedisToken> array) {
		return new Request(server, session, commandToken.getValue(), arrayToList(array));
	}

	private List<SafeString> arrayToList(List<RedisToken> request) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : request) {
			cmd.add(token.getValue());
		}
		return cmd;
	}

	private void processRDB(RedisToken token) {
		// RDB dump
		try {
			SafeString value = token.getValue();
			server.importRDB(array(value));
			logger.info(() -> "loaded RDB file from master");
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "error importing RDB file", e);
		}
	}

	private InputStream array(SafeString value) throws UnsupportedEncodingException {
		return new ByteBufferInputStream(value.getBytes());
	}
}

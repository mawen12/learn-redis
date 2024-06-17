package com.mawen.learn.redis.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.TinyDBCommandSuite;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.persistence.PersistenceManager;
import com.mawen.learn.redis.resp.RedisServer;
import com.mawen.learn.redis.resp.command.ICommand;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.ISession;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;
import io.netty.buffer.ByteBuf;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;

/**
 * Java Redis Implementation
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDB extends RedisServer implements ITinyDB {

	private static final Logger logger = Logger.getLogger(TinyDB.class.getName());

	private final BlockingQueue<List<RedisToken>> queue = new LinkedBlockingQueue<>();

	private final Optional<PersistenceManager> persistence;

	public TinyDB() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public TinyDB(String host, int port) {
		this(host, port, TinyDBConfig.withoutPersistence());
	}

	public TinyDB(String host, int port, TinyDBConfig config) {
		super(host, port, new TinyDBCommandSuite());
		if (config.isPersistenceActive()) {
			this.persistence = Optional.of(new PersistenceManager(this, config));
		}
		else {
			this.persistence = Optional.empty();
		}
		putValue("state", new TinyDBServerState(config.getNumDatabases()));
	}

	public void start() {
		super.start();
	}

	public void stop() {
		super.stop();

		queue.clear();

		logger.info("server stopped");
	}

	@Override
	protected void createSession(ISession session) {
		session.putValue("state", new TinyDBSessionState());
	}

	@Override
	protected void cleanSession(ISession session) {
		session.destroy();
	}

	@Override
	protected void executeCommand(ICommand command, IRequest request, IResponse response) {
		if (!isReadOnly(request.getCommand())) {
			try {
				command.execute(request, response);

				replication(request, command);
			}
			catch (RuntimeException e) {
				logger.log(Level.SEVERE, "error executing command: " + request, e);
			}
		}
		else {
			response.addError("READONLY You can't write against a read only slave");
		}
	}

	private boolean isReadOnly(String command) {
		return !isMaster() && isReadOnlyCommand(command);
	}

	private void replication(IRequest request, ICommand command) {
		if (isReadOnlyCommand(request.getCommand())) {
			List<RedisToken> array = requestToArray(request);
			if (hasSlaves()) {
				queue.add(array);
			}
			persistence.ifPresent(p -> p.append(array));
		}
	}

	private boolean isReadOnlyCommand(String command) {
		return getCommands().isPresent(command, ReadOnly.class);
	}

	private List<RedisToken> requestToArray(IRequest request) {
		List<RedisToken> array = new ArrayList<>();
		array.add(currentDbToken(request));
		array.add(commandToken(request));
		array.addAll(paramTokens(request));
		return array;
	}

	private RedisToken.StringRedisToken commandToken(IRequest request) {
		return new RedisToken.StringRedisToken(safeString(request.getCommand()));
	}

	private RedisToken.IntegerRedisToken currentDbToken(IRequest request) {
		return new RedisToken.IntegerRedisToken(getSessionState(request.getSession()).getCurrentDB());
	}

	private List<RedisToken.StringRedisToken> paramTokens(IRequest request) {
		return request.getParams().stream().map(RedisToken.StringRedisToken::new).collect(Collectors.toList());
	}

	private TinyDBSessionState getSessionState(ISession session) {
		return session.getValue("state");
	}

	@Override
	public void publish(String sourceKey, RedisToken message) {
		ISession session = getSession(sourceKey);
		if (session != null) {
			session.getContext().writeAndFlush(message);
		}
	}

	@Override
	public IDatabase getAdminDatabase() {
		return getState().getAdminDatabase();
	}

	@Override
	public IDatabase getDatabase(int i) {
		return getState().getDatabase(i);
	}

	@Override
	public void exportRDB(OutputStream output) throws IOException {
		getState().exportRDB(output);
	}

	@Override
	public void importRDB(InputStream input) throws IOException {
		getState().importRDB(input);
	}

	@Override
	public boolean isMaster() {
		return getState().isMaster();
	}

	@Override
	public void setMaster(boolean master) {
		getState().setMaster(master);
	}

	private TinyDBServerState getState() {
		return getValue("state");
	}

	private boolean hasSlaves() {
		return getState().hasSlaves();
	}

	@Override
	public List<List<RedisToken>> getCommandsToReplicate() {
		List<List<RedisToken>> current = new LinkedList<>();
		queue.drainTo(current);
		return current;
	}
}

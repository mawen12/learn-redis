package com.mawen.learn.redis.basic.replication;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDBServerState;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class MasterReplication implements Runnable {

	private static final Logger logger = Logger.getLogger(MasterReplication.class.getName());

	private static final String SELECT_COMMAND = "SELECT";
	private static final String PING_COMMAND = "PING";

	private static final DatabaseKey SLAVES_KEY = safeKey(safeString("slaves"));

	private static final int TASK_DELAY = 2;

	private final ITinyDB server;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public MasterReplication(ITinyDB server) {
		this.server = server;
	}

	public void start() {
		executor.scheduleWithFixedDelay(this, TASK_DELAY, TASK_DELAY, TimeUnit.SECONDS);
	}

	public void stop() {
		executor.shutdown();
	}

	public void addSlave(String id) {
		server.getAdminDatabase().merge(SLAVES_KEY, set(safeString(id)), (oldValue, newValue) -> {
			List<SafeString> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			merge.addAll(newValue.getValue());
			return set(merge);
		});
		logger.info(() -> "new slave: " + id);
	}

	public void removeSlave(String id) {
		getAdminDatabase().merge(SLAVES_KEY, set(safeString(id)), (oldValue, newValue) -> {
			List<SafeString> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			merge.removeAll(newValue.getValue());
			return set(merge);
		});
		logger.info(() -> "slave removed: " + id);
	}

	@Override
	public void run() {
		List<RedisToken.ArrayRedisToken> commands = createCommands();

		for (SafeString slave : getSlaves()) {
			for (RedisToken.ArrayRedisToken command : commands) {
				server.publish(slave.toString(), command);
			}
		}
	}

	private IDatabase getAdminDatabase() {
		return getServerState().getAdminDatabase();
	}

	private Set<SafeString> getSlaves() {
		return getAdminDatabase().getOrDefault(SLAVES_KEY, EMPTY_SET).getValue();
	}

	private List<RedisToken.ArrayRedisToken> createCommands() {
		List<RedisToken.ArrayRedisToken> commands = new LinkedList<>();
		commands.add(pingCommand());
		commands.addAll(commandsToReplicate());
		return commands;
	}

	private List<RedisToken.ArrayRedisToken> commandsToReplicate() {
		List<RedisToken.ArrayRedisToken> commands = new LinkedList<>();

		for (List<RedisToken> command : server.getCommandsToReplicate()) {
			commands.add(selectCommand(command.get(0)));
			commands.add(new RedisToken.ArrayRedisToken(command.stream().skip(1).collect(Collectors.toList())));
		}
		return commands;
	}

	private RedisToken.ArrayRedisToken selectCommand(RedisToken database) {
		return new RedisToken.ArrayRedisToken(asList(new RedisToken.StringRedisToken(safeString(SELECT_COMMAND)), database));
	}

	private RedisToken.ArrayRedisToken pingCommand() {
		return new RedisToken.ArrayRedisToken(asList(new RedisToken.StringRedisToken(safeString(PING_COMMAND))));
	}

	private TinyDBServerState getServerState() {
		return server.getValue("state");
	}
}

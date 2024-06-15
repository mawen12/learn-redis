package com.mawen.learn.redis.basic.replication;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.TinyDBServerState;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.command.Response;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.lang.String.*;
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

	private IDatabase getAdminDatabase() {
		return getServerState().getAdminDatabase();
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
		String commands = createCommands();

		for (SafeString slave : getSlaves()) {
			server.publish(slave.toString(), commands);
		}
	}

	private Set<SafeString> getSlaves() {
		return getAdminDatabase().getOrDefault(SLAVES_KEY, EMPTY_SET).getValue();
	}

	private String createCommands() {
		Response response = new Response();
		response.addArray(asList(PING_COMMAND));
		for (List<RedisToken> array : server.getCommands()) {
			RedisToken currentDB = array.remove(0);
			response.addArray(safeAsList(SELECT_COMMAND, valueOf(currentDB.<Integer>getValue())));
			response.addArray(toList(array));
		}
		return response.toString();
	}

	private List<SafeString> toList(List<RedisToken> request) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : request) {
			cmd.add(token.getValue());
		}
		return cmd;
	}

	private TinyDBServerState getServerState() {
		return server.getValue("state");
	}
}

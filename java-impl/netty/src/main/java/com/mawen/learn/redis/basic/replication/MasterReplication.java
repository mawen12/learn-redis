package com.mawen.learn.redis.basic.replication;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.redis.RedisArray;
import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
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

	private static final String SLAVES_KEY = "slaves";

	private static final int TASK_DELAY = 2;

	private final IServerContext server;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public MasterReplication(IServerContext server) {
		this.server = server;
	}

	public void start() {
		executor.scheduleWithFixedDelay(this, TASK_DELAY, TASK_DELAY, TimeUnit.SECONDS);
	}

	public void stop() {
		executor.shutdown();
	}

	public void addSlave(String id) {
		server.getAdminDatabase().merge(SLAVES_KEY, set(id), (oldValue, newValue) -> {
			List<String> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			merge.addAll(newValue.getValue());
			return set(merge);
		});
		logger.info(() -> "new slave: " + id);
	}

	public void removeSlave(String id) {
		server.getAdminDatabase().merge(SLAVES_KEY, set(id), (oldValue, newValue) -> {
			List<String> merge = new LinkedList<>();
			merge.addAll(oldValue.getValue());
			merge.removeAll(newValue.getValue());
			return set(merge);
		});
		logger.info(() -> "slave removed: " + id);
	}

	@Override
	public void run() {
		String commands = createCommands();

		for (String slave : getSlaves()) {
			server.publish(slave, commands);
		}
	}

	private String createCommands() {
		Response response = new Response();
		response.addArray(asList(PING_COMMAND));
		for (RedisArray array : server.getCommands()) {
			RedisToken currentDB = array.remove(0);
			response.addArray(safeAsList(SELECT_COMMAND, valueOf(currentDB.<Integer>getValue())));
			response.addArray(toList(array));
		}
		return response.toString();
	}

	private Set<String> getSlaves() {
		return server.getAdminDatabase().getOrDefault(SLAVES_KEY, EMPTY_SET).getValue();
	}

	private List<SafeString> toList(RedisArray request) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : request) {
			cmd.add(token.<SafeString>getValue());
		}
		return cmd;
	}
}

package com.mawen.learn.redis.basic.replication;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mawen.learn.redis.basic.command.IRequest;
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
public class MasterReplication implements Runnable{

	private final IServerContext server;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public MasterReplication(IServerContext server) {
		this.server = server;
	}

	public void start() {
		executor.scheduleWithFixedDelay(this, 5, 5, TimeUnit.SECONDS);
	}

	public void addSlave(String id) {
		server.getAdminDatabase().merge("slaves", set(id), (oldValue, newValue) -> {
			List<String> merge = new LinkedList<>();
			merge.addAll(newValue.getValue());
			merge.addAll(oldValue.getValue());
			return set(merge);
		});
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
		response.addArray(asList("PING"));
		for (RedisArray array : server.getCommands()) {
			RedisToken currentDB = array.remove(0);
			response.addArray(safeAsList("SELECT", valueOf(currentDB.<Integer>getValue())));
			response.addArray(toList(array));
		}
		return response.toString();
	}

	private Set<String> getSlaves() {
		return server.getAdminDatabase().getOrDefault("slaves", set()).getValue();
	}

	private List<SafeString> toList(RedisArray request) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : request) {
			cmd.add(token.<SafeString>getValue());
		}
		return cmd;
	}
}

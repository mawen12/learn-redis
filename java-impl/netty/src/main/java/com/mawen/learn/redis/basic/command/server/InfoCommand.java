package com.mawen.learn.redis.basic.command.server;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.command.IServerContext;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.lang.String.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/11
 */
@ReadOnly
@Command("info")
public class InfoCommand implements ITinyDBCommand {

	private static final String SHARP = "#";
	private static final String SEPARATOR = ":";
	private static final String DELIMITER = "\r\n";

	private static final String SECTION_KEYSPACE = "keyspace";
	private static final String SECTION_COMMANDSTATS = "commandstats";
	private static final String SECTION_CPU = "cpu";
	private static final String SECTION_STATS = "stats";
	private static final String SECTION_PERSISTENCE = "persistence";
	private static final String SECTION_MEMORY = "memory";
	private static final String SECTION_CLIENTS = "clients";
	private static final String SECTION_REPLICATION = "replication";
	private static final String SECTION_SERVER = "server";


	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		Map<String, Map<String, String>> sections = new HashMap<>();
		Optional<SafeString> param = request.getOptionalParam(0);
		if (param.isPresent()) {
			String sectionName = param.get().toString();
			sections.put(sectionName, section(sectionName, request.getServerContext()));
		}
		else {
			for (String section : allSections()) {
				sections.put(section, section(section, request.getServerContext()));
			}
		}
		response.addBulkStr(safeString(makeString(sections)));
	}

	private String makeString(Map<String, Map<String, String>> sections) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Map<String, String>> section : sections.entrySet()) {
			sb.append(SHARP).append(section.getKey()).append(DELIMITER);
			for (Map.Entry<String, String> entry : section.getValue().entrySet()) {
				sb.append(entry.getKey()).append(SEPARATOR).append(entry.getValue()).append(DELIMITER);
			}
			sb.append(DELIMITER);
		}
		sb.append(DELIMITER);
		return sb.toString();
	}

	private List<String> allSections() {
		return asList(SECTION_SERVER, SECTION_REPLICATION, SECTION_CLIENTS, SECTION_MEMORY, SECTION_PERSISTENCE,
				SECTION_STATS, SECTION_CPU, SECTION_COMMANDSTATS, SECTION_KEYSPACE);
	}

	private Map<String, String> section(String section, IServerContext ctx) {
		switch (section.toLowerCase()) {
			case SECTION_SERVER:
				return server(ctx);
			case SECTION_REPLICATION:
				return replication(ctx);
			case SECTION_CLIENTS:
				return clients(ctx);
			case SECTION_MEMORY:
				return memory(ctx);
			case SECTION_PERSISTENCE:
				return persistence(ctx);
			case SECTION_STATS:
				return stats(ctx);
			case SECTION_CPU:
				return cpu(ctx);
			case SECTION_COMMANDSTATS:
				return commandstats(ctx);
			case SECTION_KEYSPACE:
				return keyspace(ctx);
			default:
				break;
		}
		return null;
	}

	private Map<String, String> server(IServerContext ctx) {
		return map(entry("redis_version", "2.8.21"),
				entry("tcp_port", valueOf(ctx.getPort())));
	}

	private Map<String, String> replication(IServerContext ctx) {
		return map(entry("role", getServerState(ctx).isMaster() ? "master" : "slave"),
				entry("connected_slaves", slaves(ctx)));
	}

	private String slaves(IServerContext ctx) {
		DatabaseValue slaves = getAdminDatabase(ctx).getOrDefault("slaves", DatabaseValue.EMPTY_SET);
		return String.valueOf(slaves.<Set<String>>getValue().size());
	}

	private Map<String, String> clients(IServerContext ctx) {
		return map(entry("connected_clients", valueOf(ctx.getClients())));
	}

	private Map<String, String> memory(IServerContext ctx) {
		return map(entry("used_memory", valueOf(Runtime.getRuntime().totalMemory())));
	}

	private Map<String, String> persistence(IServerContext ctx) {
		// TODO
		return map();
	}

	private Map<String, String> stats(IServerContext ctx) {
		// TODO
		return map();
	}

	private Map<String, String> cpu(IServerContext ctx) {
		// TODO
		return map();
	}

	private Map<String, String> commandstats(IServerContext ctx) {
		// TODO
		return map();
	}

	private Map<String, String> keyspace(IServerContext ctx) {
		// TODO
		return map();
	}

	@SafeVarargs
	private static Map<String, String> map(Map.Entry<String, String>... values) {
		return Stream.of(values).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static Map.Entry<String, String> entry(String key, String value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
}

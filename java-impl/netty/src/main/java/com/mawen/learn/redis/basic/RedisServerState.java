package com.mawen.learn.redis.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.persistence.RDBInputStream;
import com.mawen.learn.redis.basic.persistence.RDBOutputStream;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class RedisServerState {

	private static final String SLAVES_KEY = "slaves";

	private boolean master;
	private final List<IDatabase> databases = new ArrayList<>();
	private final IDatabase admin = new Database();

	public RedisServerState(int numDatabases) {
		this.master = true;
		for (int i = 0; i < numDatabases; i++) {
			databases.add(new Database());
		}
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public boolean isMaster() {
		return master;
	}

	public IDatabase getAdminDatabase() {
		return admin;
	}

	public IDatabase getDatabase(int i) {
		return databases.get(i);
	}

	public void clear() {
		databases.clear();
	}

	public boolean hasSlaves() {
		return !admin.getOrDefault(SLAVES_KEY, DatabaseValue.EMPTY_SET).<Set<String>>getValue().isEmpty();
	}

	public void exportRDB(OutputStream output) throws IOException {
		RDBOutputStream rdb = new RDBOutputStream(output);

		 rdb.preamble(6);
		for (int i = 0; i < databases.size(); i++) {
			IDatabase db = databases.get(i);
			if (!db.isEmpty()) {
				rdb.select(i);
				rdb.database(db);
			}
		}
		rdb.end();
	}

	public void importRDB(InputStream input) throws IOException {
		RDBInputStream rdb = new RDBInputStream(input);

		for (Map.Entry<Integer, IDatabase> entry : rdb.parse().entrySet()) {
			databases.set(entry.getKey(), entry.getValue());
		}
	}
}

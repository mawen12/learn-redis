package com.mawen.learn.redis.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.data.SimpleDatabase;
import com.mawen.learn.redis.basic.persistence.RDBInputStream;
import com.mawen.learn.redis.basic.persistence.RDBOutputStream;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class TinyDBServerState {

	private static final int RDB_VERSION = 6;

	private static final DatabaseKey SLAVES_KEY = safeKey(safeString("slaves"));

	private boolean master;
	private final List<IDatabase> databases = new ArrayList<>();
	private final IDatabase admin = new SimpleDatabase();

	public TinyDBServerState(int numDatabases) {
		this.master = true;
		for (int i = 0; i < numDatabases; i++) {
			databases.add(new SimpleDatabase());
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
		DatabaseValue slaves = admin.getOrDefault(SLAVES_KEY, DatabaseValue.EMPTY_SET);
		return !slaves.<Set<String>>getValue().isEmpty();
	}

	public void exportRDB(OutputStream output) throws IOException {
		RDBOutputStream rdb = new RDBOutputStream(output);

		rdb.preamble(RDB_VERSION);
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

		rdb.parse().forEach(this.databases::set);
	}
}

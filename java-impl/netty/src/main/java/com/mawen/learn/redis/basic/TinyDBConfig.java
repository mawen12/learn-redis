package com.mawen.learn.redis.basic;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class TinyDBConfig {

	private static final int DEFAULT_SYNC_PERIOD = 30;
	private static final int DEFAULT_DATABASES = 10;
	private static final String DUMP_FILE = "dump.rdb";
	private static final String REDO_FILE = "redo.aof";

	private int numDatabases;

	private boolean persistenceActive;

	private String rdbFile;
	private String aofFile;

	private int syncPeriod;

	public boolean isPersistenceActive() {
		return persistenceActive;
	}

	public void setPersistenceActive(boolean persistenceActive) {
		this.persistenceActive = persistenceActive;
	}

	public String getRdbFile() {
		return rdbFile;
	}

	public void setRdbFile(String rdbFile) {
		this.rdbFile = rdbFile;
	}

	public String getAofFile() {
		return aofFile;
	}

	public void setAofFile(String aofFile) {
		this.aofFile = aofFile;
	}

	public int getSyncPeriod() {
		return syncPeriod;
	}

	public void setSyncPeriod(int syncPeriod) {
		this.syncPeriod = syncPeriod;
	}

	public int getNumDatabases() {
		return numDatabases;
	}

	public void setNumDatabases(int numDatabases) {
		this.numDatabases = numDatabases;
	}

	public static TinyDBConfig withoutPersistence() {
		TinyDBConfig config = new TinyDBConfig();
		config.setNumDatabases(DEFAULT_DATABASES);
		config.setPersistenceActive(true);
		return config;
	}

	public static TinyDBConfig withPersistence() {
		TinyDBConfig config = new TinyDBConfig();
		config.setNumDatabases(DEFAULT_DATABASES);
		config.setPersistenceActive(true);
		config.setRdbFile(DUMP_FILE);
		config.setAofFile(REDO_FILE);
		config.setSyncPeriod(DEFAULT_SYNC_PERIOD);
		return config;
	}
}

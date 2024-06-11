package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CheckedInputStream;

import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.persistence.Util.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/11
 */
public class RDBInputStream {

	private static final int HASH = 0x04;
	private static final int SORTED_SET = 0x03;
	private static final int SET = 0x02;
	private static final int LIST = 0x01;
	private static final int STRING = 0x00;
	private static final int TTL_MILISECONDS = 0xFC;
	private static final int TTL_SECONDS = 0xFD;
	private static final int SELECT = 0xFE;
	private static final int END_OF_STREAM = 0xFF;
	private static final int VERSION_LENGTH = 4;
	private static final int REDIS_LENGTH = 5;

	private final CheckedInputStream in;

	public RDBInputStream(InputStream in) {
		super();
		this.in = new CheckedInputStream(in, new CRC64());
	}

	public Map<Integer, IDatabase> parse() throws IOException {
		Map<Integer, IDatabase> databases = new HashMap<>();

		int version = version();

		if (version > 6) {
			throw new IOException("invalid version: " + version);
		}

		for (boolean end = false; !end; ) {
			IDatabase db = null;
			int read = in.read();
			switch (read) {
				case SELECT:
					// select db
					db = new Database(new HashMap<>());
					databases.put(parseLength(), db);
					break;
				case TTL_SECONDS:
					// TODO: TTL in seconds
					break;
				case TTL_MILISECONDS:
					// TODO: TTL in miliseconds
					break;
				case STRING:
					parseString(db);
					break;
				case LIST:
					parseList(db);
					break;
				case SET:
					parseSet(db);
					break;
				case SORTED_SET:
					parseSortedSet(db);
					break;
				case HASH:
					parseHash(db);
					break;
				case END_OF_STREAM:
					// end of stream
					end = true;
					break;
				default:
					throw new IOException("not supported: " + read);
			}
		}

		verifyChecksum();

		return databases;
	}

	private void verifyChecksum() throws IOException {
		long calculated = in.getChecksum().getValue();

		long readed = parseCheckSum();

		if (calculated != readed) {
			throw new IOException("invalid checksum");
		}
	}

	private long parseCheckSum() throws IOException {
		byte[] buf = new byte[Long.BYTES];
		in.read(buf);
		return Util.byteArrayToLong(buf);
	}

	private int version() throws IOException {
		byte[] redis = new byte[REDIS_LENGTH];
		in.read(redis);
		byte[] version = new byte[VERSION_LENGTH];
		in.read(version);
		return parseVersion(version);
	}

	private int parseVersion(byte[] version) {
		StringBuilder sb = new StringBuilder();
		for (byte b : version) {
			sb.append((char) b);
		}
		return Integer.parseInt(sb.toString());
	}

	private void parseString(IDatabase db) throws IOException {
		String key = parseString();
		String value = parseString();
		ensure(db, key, string(value));
	}

	private void parseList(IDatabase db) throws IOException {
		String key = parseString();
		int size = parseLength();
		List<String> list = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			list.add(parseString());
		}
		ensure(db, key, list(list));
	}

	private void parseSet(IDatabase db) throws IOException {
		String key = parseString();
		int size = parseLength();
		Set<String> set = new HashSet<>();
		for (int i = 0; i < size; i++) {
			set.add(parseString());
		}
		ensure(db, key, set(set));
	}

	private void parseSortedSet(IDatabase db) throws IOException {
		String key = parseString();
		int size = parseLength();
		Set<String> set = new LinkedHashSet<>();
		for (int i = 0; i < size; i++) {
			set.add(parseString());
		}
		ensure(db, key, set(set));
	}

	private void parseHash(IDatabase db) throws IOException {
		String key = parseString();
		int size = parseLength();
		Set<Map.Entry<String, String>> entries = new HashSet<>();
		for (int i = 0; i < size; i++) {
			entries.add(entry(parseString(), parseString()));
		}
		ensure(db, key, hash(entries));
	}

	private void ensure(IDatabase db, String key, DatabaseValue value) throws IOException {
		if (db != null) {
			db.put(key, value);
		}
		else {
			throw new IOException("no database selected");
		}
	}

	private Double parseDouble() throws IOException {
		return Double.parseDouble(parseString());
	}

	private String parseString() throws IOException {
		int length = parseLength();
		byte[] buf = new byte[length];
		in.read(buf);
		return new String(buf, StandardCharsets.UTF_8);
	}

	private int parseLength() throws IOException {
		int length = in.read();
		if (length < 0x40) {
			// 1 byte: 00XXXXXX
			return length;
		}
		else if (length < 0x4000) {
			// 2 bytes: 01XXXXXX XXXXXXXX
			int next = in.read();
			return ((length & 0x3F) << 8) | (next & 0xFf);
		}
		else {
			// 5 bytes: 10...... XXXXXXXX XXXXXXXX XXXXXXXX XXXXXXXX
			byte[] array = new byte[Integer.BYTES];
			in.read(array);
			return byteArrayToInt(array);
		}
	}
}

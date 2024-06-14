package com.mawen.learn.redis.basic.persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.zip.CheckedOutputStream;

import com.mawen.learn.redis.basic.data.DataType;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.persistence.ByteUtils.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/11
 */
public class RDBOutputStream {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final byte[] REDIS = safeString("REDIS").getBytes();

	private static final int TTL_MILLISECONDS = 0xFC;
	private static final int END_OF_STREAM = 0xFF;
	private static final int SELECT = 0xFE;

	private final CheckedOutputStream out;

	public RDBOutputStream(OutputStream out) {
		super();
		this.out = new CheckedOutputStream(out, new CRC64());
	}

	public void preamble(int version) throws IOException {
		out.write(REDIS);
		out.write(version(version));
	}

	private byte[] version(int version) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(String.valueOf(version));
		for (int i = sb.length(); i < Integer.BYTES; i++) {
			sb.insert(0, '0');
		}
		return sb.toString().getBytes(DEFAULT_CHARSET);
	}

	public void select(int db) throws IOException {
		out.write(SELECT);
		length(db);
	}

	public void database(IDatabase db) throws IOException {
		for (Map.Entry<DatabaseKey, DatabaseValue> entry : db.entrySet()) {
			value(entry.getKey(), entry.getValue());
		}
	}

	private void value(DatabaseKey key, DatabaseValue value) throws IOException {
		expiredAt(key.expiredAt());
		type(value.getType());
		key(key);
		value(value);
	}

	private void expiredAt(Long expiredAt) throws IOException {
		if (expiredAt != null) {
			out.write(TTL_MILLISECONDS);
			out.write(ByteUtils.toByteArray(expiredAt));
		}
	}

	private void type(DataType type) throws IOException {
		out.write(type.ordinal());
	}

	private void key(DatabaseKey key) throws IOException {
		string(key.getValue());
	}

	private void value(DatabaseValue value) throws IOException {
		switch (value.getType()) {
			case STRING:
				string(value.<SafeString>getValue());
				break;
			case LIST:
				list(value.getValue());
				break;
			case HASH:
				hash(value.getValue());
				break;
			case SET:
				set(value.getValue());
				break;
			case ZSET:
				zset(value.getValue());
				break;
			default:
				break;
		}
	}

	private void length(int length) throws IOException {
		if (length < 0x40) {
			// 1 byte: 00XXXXXX
			out.write(length);
		}
		else if (length < 0x4000) {
			// 2 bytes: 01XXXXXX XXXXXXXXX
			out.write(0x4000 & length);
		}
		else {
			// 5 bytes: 10...... XXXXXXXX XXXXXXXX XXXXXXXX XXXXXXXX
			out.write(0x80);
			out.write(toByteArray(length));
		}
	}

	private void string(String value) throws IOException {
		string(safeString(value));
	}

	private void string(double value) throws IOException {
		string(String.valueOf(value));
	}

	private void string(SafeString value) throws IOException {
		byte[] bytes = value.getBytes();
		length(bytes.length);
		out.write(bytes);
	}

	private void list(List<SafeString> value) throws IOException {
		length(value.size());
		for (SafeString item : value) {
			string(item);
		}
	}

	private void hash(Map<SafeString, SafeString> value) throws IOException {
		length(value.size());
		for (Map.Entry<SafeString, SafeString> entry : value.entrySet()) {
			string(entry.getKey());
			string(entry.getValue());
		}
	}

	private void set(Set<SafeString> value) throws IOException {
		length(value.size());
		for (SafeString item : value) {
			string(item);
		}
	}

	private void zset(NavigableSet<Map.Entry<Double, SafeString>> value) throws IOException {
		length(value.size());
		for (Map.Entry<Double, SafeString> item : value) {
			string(item.getValue());
			string(item.getKey());
		}
	}

	public void end() throws IOException {
		out.write(END_OF_STREAM);
		out.write(toByteArray(out.getChecksum().getValue()));
		out.flush();
	}
}

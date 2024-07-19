package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.mawen.learn.redis.resp.protocol.RedisToken.*;


/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class RedisParser {

	private static final byte STRING_PREFIX = '$';
	private static final byte INTEGER_PREFIX = ':';
	private static final byte ERROR_PREFIX = '-';
	private static final byte STATUS_PREFIX = '+';
	private static final byte ARRAY_PREFIX = '*';

	private final int maxLength;
	private final RedisSource source;

	public RedisParser(int maxLength, RedisSource source) {
		this.maxLength = maxLength;
		this.source = source;
	}

	public RedisToken parse() {
		return parseToken(source.readLine());
	}

	private RedisToken parseToken(SafeString line) {
		RedisToken token = new UnknownRedisToken(SafeString.EMPTY_STRING);

		if (line != null && !line.isEmpty()) {
			if (line.startsWith(ARRAY_PREFIX)) {
				int size = Integer.parseInt(line.substring(1));
				token = parseArray(size);
			}
			else if (line.startsWith(STATUS_PREFIX)) {
				token = status(line.substring(1));
			}
			else if (line.startsWith(ERROR_PREFIX)) {
				token = status(line.substring(1));
			}
			else if (line.startsWith(INTEGER_PREFIX)) {
				token = parseIntegerToken(line);
			}
			else if (line.startsWith(STRING_PREFIX)) {
				token = parseStringToken(line);
			}
			else {
				token = new UnknownRedisToken(line);
			}
		}

		return token;
	}

	private RedisToken parseIntegerToken(SafeString line) {
		Integer value = Integer.valueOf(line.substring(1));
		return new IntegerRedisToken(value);
	}

	private RedisToken parseStringToken(SafeString line) {
		RedisToken token;
		int length = Integer.parseInt(line.substring(1));
		if (length > 0 && length < maxLength) {
			token = new StringRedisToken(source.readString(length));
			source.readLine();
		}
		else {
			token = new StringRedisToken(SafeString.EMPTY_STRING);
		}
		return token;
	}

	private ArrayRedisToken parseArray(int size) {
		List<RedisToken> array = new ArrayList<>(size);

		for (int i = 0; i < size; i++) {
			array.add(parseToken(source.readLine()));
		}

		return new ArrayRedisToken(array);
	}
}

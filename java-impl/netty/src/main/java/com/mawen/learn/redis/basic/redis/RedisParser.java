package com.mawen.learn.redis.basic.redis;

import java.nio.ByteBuffer;

import static com.mawen.learn.redis.basic.redis.RedisToken.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class RedisParser {

	private static final String STRING_PREFIX = "$";
	private static final String INTEGER_PREFIX = ":";
	private static final String ERROR_PREFIX = "-";
	private static final String STATUS_PREFIX = "+";
	private static final String ARRAY_PREFIX = "*";

	private RedisSource source;

	public RedisParser(RedisSource source) {
		this.source = source;
	}

	public RedisToken parse() {
		String line = source.readLine();

		RedisToken token = null;

		if (line != null && !line.isEmpty()) {
			if (line.startsWith(ARRAY_PREFIX)) {
				int size = Integer.parseInt(line.substring(1));
				token = parseArray(size);
			}
			else if (line.startsWith(STATUS_PREFIX)) {
				token  = new StatusRedisToken(line.substring(1));
			}
			else if (line.startsWith(ERROR_PREFIX)) {
				token = new ErrorRedisToken(line.substring(1));
			}
			else if (line.startsWith(INTEGER_PREFIX)) {
				Integer value = Integer.valueOf(line.substring(1));
				token = new IntegerRedisToken(value);
			}
			else if (line.startsWith(STRING_PREFIX)) {
				int length = Integer.parseInt(line.substring(1));
				ByteBuffer bulk = source.readBytes(length);
				token = new StringRedisToken(new SafeString(bulk));
				source.readLine();
			}
			else {
				token = new UnknownRedisToken(line);
			}
		}

		return token;
	}

	private ArrayRedisToken parseArray(int size) {
		RedisArray array = new RedisArray();

		for (int i = 0; i < size; i++) {
			String line = source.readLine();

			if (line != null) {
				if (line.startsWith(STRING_PREFIX)) {
					int length = Integer.parseInt(line.substring(1));
					ByteBuffer bulk = source.readBytes(length);
					array.add(new StringRedisToken(new SafeString(bulk)));
					source.readLine();
				}
				else if (line.startsWith(INTEGER_PREFIX)) {
					// integer
					Integer value = Integer.valueOf(line.substring(1));
					array.add(new IntegerRedisToken(value));
				}
			}
		}

		return new ArrayRedisToken(array);
	}
}

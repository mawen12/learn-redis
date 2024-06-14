package com.mawen.learn.redis.resp.protocol;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public enum RedisTokenType {

	STATUS(false),
	INTEGER(false),
	STRING(false),
	ARRAY(false),
	ERROR(true),
	UNKNOWN(true);

	private final boolean error;

	RedisTokenType(boolean error) {
		this.error = error;
	}

	public boolean isError() {
		return error;
	}
}

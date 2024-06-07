package com.mawen.learn.redis.basic.data;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public enum DataType {

	STRING("string"),
	LIST("list"),
	SET("set"),
	ZSET("zset"),
	HASH("hash"),
	NONE("none");

	private final String text;

	DataType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}

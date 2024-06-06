package com.mawen.learn.redis.basic.data;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DatabaseValue {

	private DataType type;

	private String value;

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int incrementAndGet() throws NumberFormatException {
		int i = Integer.parseInt(value);
		this.value = String.valueOf(++i);
		return i;
	}

	public int decrementAndGet() throws NumberFormatException {
		int i = Integer.parseInt(value);
		this.value = String.valueOf(--i);
		return i;
	}
}

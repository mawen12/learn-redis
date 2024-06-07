package com.mawen.learn.redis.basic.data;

import java.io.Serializable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DatabaseValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private DataType type;

	private Serializable value;

	public DatabaseValue(DataType type) {
		this(type, null);
	}

	public DatabaseValue(DataType type, Serializable value) {
		this.type = type;
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public <T extends Serializable> T getValue() {
		return (T) value;
	}

	public <T extends Serializable> void setValue(T value) {
		this.value = value;
	}

	public int incrementAndGet(int increment) throws NumberFormatException {
		int i = Integer.parseInt(value.toString()) + increment;
		this.value = String.valueOf(i);
		return i;
	}

	public int decrementAndGet(int decrement) throws NumberFormatException {
		int i = Integer.parseInt(value.toString()) - decrement;
		this.value = String.valueOf(i);
		return i;
	}
}

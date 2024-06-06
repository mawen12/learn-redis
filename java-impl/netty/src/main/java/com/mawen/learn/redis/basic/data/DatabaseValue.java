package com.mawen.learn.redis.basic.data;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class DatabaseValue {

	private DataType type;

	private Object value;

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
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

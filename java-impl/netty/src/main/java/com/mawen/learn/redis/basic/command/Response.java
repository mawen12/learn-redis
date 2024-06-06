package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.data.DatabaseValue;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Response implements IResponse {

	private static final String ARRAY = "*";
	private static final String ERROR = "-";
	private static final String INTEGER = ":";
	private static final String SIMPLE_STRING = "+";
	private static final String BULK_STRING = "$";

	private final StringBuilder sb = new StringBuilder();

	@Override
	public IResponse addValue(DatabaseValue value) {
		switch (value.getType()) {
			case STRING:
				addBulkStr(value.getValue());
				break;
			case INTEGER:
				addInt(value.getValue());
				break;
			default:
				break;
		}

		return this;
	}

	@Override
	public IResponse addBulkStr(String str) {
		if (str != null) {
			sb.append(BULK_STRING).append(str.length()).append(ICommand.DELIMITER).append(str);
		}
		else {
			sb.append(BULK_STRING).append(-1);
		}
		return this;
	}

	@Override
	public IResponse addSimpleStr(String str) {
		sb.append(SIMPLE_STRING).append(str);
		return this;
	}

	@Override
	public IResponse addInt(int i) {
		sb.append(INTEGER).append(i);
		return this;
	}

	@Override
	public IResponse addInt(boolean b) {
		sb.append(INTEGER).append(b ? 1 : 0);
		return this;
	}

	@Override
	public IResponse addInt(String str) {
		sb.append(INTEGER).append(str);
		return this;
	}

	@Override
	public IResponse addError(String str) {
		sb.append(ERROR).append(str);
		return this;
	}

	@Override
	public IResponse addArray(String[] array) {
		if (array != null) {
			sb.append(ARRAY).append(array.length);
			for (String str : array) {
				addBulkStr(str);
			}
		}
		else {
			sb.append(ARRAY).append(0);
		}
		return this;
	}

	@Override
	public String toString() {
		return sb.toString() + ICommand.DELIMITER;
	}
}

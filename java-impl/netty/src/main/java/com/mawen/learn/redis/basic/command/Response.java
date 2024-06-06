package com.mawen.learn.redis.basic.command;

import java.util.Collection;

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

	public static final String DELIMITER = "\r\n";


	private final StringBuilder sb = new StringBuilder();

	@Override
	public IResponse addValue(DatabaseValue value) {
		if (value != null) {
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
		}
		else {
			addBulkStr(null);
		}
		return this;
	}

	@Override
	public IResponse addBulkStr(String str) {
		if (str != null) {
			sb.append(BULK_STRING).append(str.length()).append(DELIMITER).append(str);
		}
		else {
			sb.append(BULK_STRING).append(-1);
		}
		sb.append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addSimpleStr(String str) {
		sb.append(SIMPLE_STRING).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(int i) {
		sb.append(INTEGER).append(i).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(boolean b) {
		sb.append(INTEGER).append(b ? 1 : 0).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addInt(String str) {
		sb.append(INTEGER).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addError(String str) {
		sb.append(ERROR).append(str).append(DELIMITER);
		return this;
	}

	@Override
	public IResponse addArray(Collection<DatabaseValue> array) {
		if (array != null) {
			sb.append(ARRAY).append(array.size()).append(DELIMITER);
			for (DatabaseValue value : array) {
				addValue(value);
			}
		}
		else {
			sb.append(ARRAY).append(0).append(DELIMITER);
		}
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
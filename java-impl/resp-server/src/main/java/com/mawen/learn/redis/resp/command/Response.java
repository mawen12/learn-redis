package com.mawen.learn.redis.resp.command;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class Response implements IResponse {

	private boolean exit;

	private RedisToken token;

	@Override
	public IResponse addBulkStr(SafeString str) {
		token = RedisToken.string(str);
		return this;
	}

	@Override
	public IResponse addSimpleStr(String str) {
		token = RedisToken.status(str);
		return this;
	}

	@Override
	public IResponse addInt(int value) {
		token = RedisToken.integer(value);
		return this;
	}

	@Override
	public IResponse addInt(boolean value) {
		token = RedisToken.integer(value ? 1 : 0);
		return this;
	}

	@Override
	public IResponse addError(String str) {
		token = RedisToken.error(str);
		return this;
	}

	@Override
	public IResponse addArray(Collection<?> array) {
		if (array == null) {
			token = RedisToken.array();
		}
		else {
			List<RedisToken> tokens = new ArrayList<>(array.size());
			for (Object value : array) {
				if (value instanceof Integer) {
					tokens.add(RedisToken.integer((Integer) value));
				}
				else if (value instanceof Boolean){
					Boolean b = (Boolean) value;
					tokens.add(RedisToken.integer(b ? 1 : 0));
				}
				else if (value instanceof String) {
					tokens.add(RedisToken.string((String) value));
				}
				else if (value instanceof SafeString) {
					tokens.add(RedisToken.string((SafeString) value));
				}
				else if (value instanceof RedisToken) {
					tokens.add((RedisToken) value);
				}
			}
			token = RedisToken.array(tokens);
		}
		return this;
	}

	@Override
	public RedisToken build() {
		return token;
	}

	@Override
	public void exit() {
		this.exit = true;
	}

	@Override
	public boolean isExit() {
		return exit;
	}

	@Override
	public String toString() {
		return Objects.toString(token);
	}
}

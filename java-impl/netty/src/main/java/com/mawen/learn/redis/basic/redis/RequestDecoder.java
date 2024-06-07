package com.mawen.learn.redis.basic.redis;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RequestDecoder extends LineBasedFrameDecoder {

	private static final String ARRAY_PREFIX = "*";
	private static final String STATUS_PREFIX = "+";
	private static final String ERROR_PREFIX = "-";
	private static final String STRING_PREFIX = "$";
	private static final String INTEGER_PREFIX = ":";

	public RequestDecoder(int maxLength) {
		super(maxLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		return parseResponse(ctx, buffer);
	}

	private RedisToken<?> parseResponse(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		String line = readLine(ctx, buffer);

		RedisToken<?> token = null;

		if (line != null) {
			if (line.startsWith(ARRAY_PREFIX)) {
				// array
				int size = Integer.parseInt(line.substring(1));
				token = parseArray(ctx, buffer, size);
			}
			else if (line.startsWith(STATUS_PREFIX)) {
				// simple string
				token = new RedisToken.StatusRedisToken(line.substring(1));
			}
			else if (line.startsWith(ERROR_PREFIX)) {
				// error
				token = new RedisToken.ErrorRedisToken(line.substring(1));
			}
			else if (line.startsWith(INTEGER_PREFIX)) {
				// integer
				Integer value = Integer.valueOf(line.substring(1));
				token = new RedisToken.IntegerRedisToken(value);
			}
			else if (line.startsWith(STRING_PREFIX)) {
				// bulk string
				String value = readLine(ctx, buffer);
				token = new RedisToken.StringRedisToken(value);
			}
			else {
				token = new RedisToken.UnknownRedisToken(line);
			}
		}

		return token;
	}

	private String readLine(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		ByteBuf readLine = (ByteBuf) super.decode(ctx, buffer);

		if (readLine != null) {
			try {
				return readLine.toString(StandardCharsets.UTF_8);
			}
			finally {
				readLine.release();
			}
		}
		else {
			return null;
		}
	}

	private RedisToken.ArrayRedisToken parseArray(ChannelHandlerContext ctx, ByteBuf buffer, int size) throws Exception {
		List<RedisToken<?>> response = new LinkedList<>();

		for (int i = 0; i < size; i++) {
			String line = readLine(ctx, buffer);

			if (line != null) {
				if (line.startsWith(STRING_PREFIX)) {
					// bulk string
					String value = readLine(ctx, buffer);
					response.add(new RedisToken.StringRedisToken(value));
				}
				else if (line.startsWith(INTEGER_PREFIX)) {
					// integer
					Integer value = Integer.valueOf(line.substring(1));
					response.add(new RedisToken.IntegerRedisToken(value));
				}
			}
		}

		return new RedisToken.ArrayRedisToken(response);
	}
}

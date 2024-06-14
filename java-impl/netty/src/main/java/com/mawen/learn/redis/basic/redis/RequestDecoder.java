package com.mawen.learn.redis.basic.redis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

import static com.mawen.learn.redis.basic.redis.RedisToken.*;

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

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final int maxLength;

	public RequestDecoder(int maxLength) {
		super(maxLength);
		this.maxLength = maxLength;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		return parseResponse(ctx, buffer);
	}

	private RedisToken parseResponse(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		String line = readLine(ctx, buffer);

		RedisToken token = null;

		if (line != null) {
			if (line.startsWith(ARRAY_PREFIX)) {
				int size = Integer.parseInt(line.substring(1));
				token = parseArray(ctx, buffer, size);
			}
			else if (line.startsWith(STATUS_PREFIX)) {
				token = new StatusRedisToken(line.substring(1));
			}
			else if (line.startsWith(ERROR_PREFIX)) {
				token = new ErrorRedisToken(line.substring(1));
			}
			else if (line.startsWith(INTEGER_PREFIX)) {
				token = parseIntegerToken(line);
			}
			else if (line.startsWith(STRING_PREFIX)) {
				token = parseStringToken(ctx, buffer, line);
			}
			else {
				token = new UnknownRedisToken(line);
			}
		}

		return token;
	}

	private String readLine(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		ByteBuf readLine = (ByteBuf) super.decode(ctx, buffer);

		if (readLine != null) {
			try {
				return readLine.toString(DEFAULT_CHARSET);
			}
			finally {
				readLine.release();
			}
		}
		else {
			return null;
		}
	}

	private ArrayRedisToken parseArray(ChannelHandlerContext ctx, ByteBuf buffer, int size) throws Exception {
		RedisArray array = new RedisArray();

		for (int i = 0; i < size; i++) {
			String line = readLine(ctx, buffer);

			if (line != null) {
				if (line.startsWith(STRING_PREFIX)) {
					array.add(parseStringToken(ctx, buffer, line));
				}
				else if (line.startsWith(INTEGER_PREFIX)) {
					array.add(parseIntegerToken(line));
				}
			}
		}

		return new ArrayRedisToken(array);
	}

	private RedisToken parseIntegerToken(String line) {
		Integer value = Integer.valueOf(line.substring(1));
		return new IntegerRedisToken(value);
	}

	private RedisToken parseStringToken(ChannelHandlerContext ctx, ByteBuf buffer, String line) throws Exception {
		RedisToken token;
		int length = Integer.parseInt(line.substring(1));
		if (length > 0 && length < maxLength) {
			ByteBuf bulk = buffer.readBytes(length);
			token = new StringRedisToken(new SafeString(bulk.nioBuffer()));
			readLine(ctx, buffer);
		}
		else {
			token = new StringRedisToken(SafeString.EMPTY_STRING);
		}
		return token;
	}
}

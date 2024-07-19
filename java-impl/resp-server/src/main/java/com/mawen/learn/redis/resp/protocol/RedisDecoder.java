package com.mawen.learn.redis.resp.protocol;

import java.io.IOError;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class RedisDecoder extends ReplayingDecoder<Void> {

	private final int maxLength;

	public RedisDecoder(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		out.add(parseResponse(ctx, buffer));
	}

	private SafeString readLine(ChannelHandlerContext ctx, ByteBuf buffer) {
		int eol = findEndOfLine(buffer);
		int size = eol - buffer.readerIndex();
		return readString(buffer, size);
	}

	private SafeString readString(ByteBuf buffer, int size) {
		SafeString safeString = readBytes(buffer, size);
		buffer.skipBytes(2);
		return safeString;
	}

	private SafeString readBytes(ByteBuf buffer, int size) {
		return new SafeString(buffer.readBytes(size).nioBuffer());
	}

	private static int findEndOfLine(final ByteBuf buffer) {
		int i = buffer.forEachByte(ByteBufProcessor.FIND_LF);
		if (i > 0 && buffer.getByte(i - 1) == '\r') {
			i--;
		}
		return i;
	}

	private RedisToken parseResponse(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		RedisParser parser = new RedisParser(maxLength, new RedisSource() {
			@Override
			public SafeString readString(int length) {
				return RedisDecoder.this.readString(buffer, length);
			}

			@Override
			public SafeString readLine() {
				return RedisDecoder.this.readLine(ctx, buffer);
			}
		});

		RedisToken token = parser.parse();
		checkpoint();
		return token;
	}
}

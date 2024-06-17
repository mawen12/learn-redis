package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;
import java.util.List;

import com.mawen.learn.redis.resp.command.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/17
 */
public class RequestEncoder extends MessageToByteEncoder<RedisToken> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, RedisToken msg, ByteBuf out) throws Exception {
		Response response = new Response();

		switch (msg.getType()) {
			case STRING:
				response.addBulkStr(msg.getValue());
				break;
			case STATUS:
				response.addSimpleStr(msg.getValue());
				break;
			case INTEGER:
				response.addInt(msg.<Integer>getValue());
				break;
			case ERROR:
				response.addError(msg.getValue());
				break;
			case ARRAY:
				response.addArray(collectValues(msg));
				break;
			case UNKNOWN:
				break;
		}
		out.writeBytes(response.getBytes());
	}

	private List<?> collectValues(RedisToken msg) {
		return msg.<List<RedisToken>>getValue().stream().map(RedisToken::getValue).collect(toList());
	}

	private static class ResponseBuilder {

		private final ByteBufferBuilder builder = new ByteBufferBuilder();

		private byte[] encodedToken(RedisToken msg) {
			switch (msg.getType()) {
				case STRING:
					addBulkStr(msg.getValue());
					break;
				case STATUS:
					addSimpleStr(msg.getValue());
					break;
				case INTEGER:
					addInt(msg.<Integer>getValue());
					break;
				case ERROR:
					addError(msg.getValue());
					break;
				case ARRAY:
					addArray(collectValues(msg));
					break;
				case UNKNOWN:
					break;
			}
			return builder.build();
		}

		private void addBulkStr(String str) {
			builder.append();
		}
	}

	private static class ByteBufferBuilder {
		private static final int INITIAL_CAPACITY = 1024;

		private ByteBuffer buffer = ByteBuffer.allocate(INITIAL_CAPACITY);

		public ByteBufferBuilder append(int i) {
			append(String.valueOf(i));
			return this;
		}

		public ByteBufferBuilder append(String str) {
			append(str.getBytes(DEFAULT_CHARSET));
			return this;
		}

		public ByteBufferBuilder append(SafeString str) {
			append(str.getBytes());
			return this;
		}

		private ByteBufferBuilder append(byte[] buf) {
			ensureCapacity(buf.length);
			buffer.put(buf);
			return this;
		}

		public ByteBufferBuilder append(byte b) {
			ensureCapacity(1);
			buffer.put(b);
			return this;
		}

		private void ensureCapacity(int len) {
			if (buffer.remaining() < len) {
				growBuffer(len);
			}
		}

		private void growBuffer(int len) {
			int capacity = buffer.capacity() + Math.max(len, INITIAL_CAPACITY);
			buffer = ByteBuffer.allocate(capacity).put(build());
		}

		public byte[] build() {
			byte[] array = new byte[buffer.position()];
			buffer.rewind();
			buffer.get(array);
			return array;
		}
	}
}
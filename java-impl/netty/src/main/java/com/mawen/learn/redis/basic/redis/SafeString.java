package com.mawen.learn.redis.basic.redis;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class SafeString {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final ByteBuffer buffer;

	public SafeString(byte[] bytes) {
		Objects.requireNonNull(bytes);
		this.buffer = ByteBuffer.wrap(bytes);
	}

	public SafeString(ByteBuffer buffer) {
		Objects.requireNonNull(buffer);
		this.buffer = buffer;
	}

	public byte[] getBytes() {
		ByteBuffer copy = buffer.duplicate();
		byte[] bytes = new byte[copy.remaining()];
		copy.get(bytes);
		return bytes;
	}

	public ByteBuffer getBuffer() {
		return buffer.duplicate();
	}

	public int length() {
		return buffer.remaining();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buffer.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SafeString other = (SafeString) obj;
		if (!buffer.equals(other.buffer)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return DEFAULT_CHARSET.decode(buffer.duplicate()).toString();
	}

	public static SafeString safeString(String str) {
		Objects.requireNonNull(str);
		return new SafeString(DEFAULT_CHARSET.encode(str));
	}

	public static List<SafeString> safeAsList(String... strs) {
		Objects.requireNonNull(strs);
		return Stream.of(strs).map(SafeString::safeString).collect(toList());
	}
}

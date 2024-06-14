package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static tonivade.equalizer.Equalizer.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
public class SafeString implements Comparable<SafeString> {

	public static final SafeString EMPTY_STRING = new SafeString(new byte[]{});

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private final ByteBuffer buffer;

	public SafeString(byte[] bytes) {
		this.buffer = ByteBuffer.wrap(requireNonNull(bytes));
	}

	public SafeString(ByteBuffer buffer) {
		this.buffer = requireNonNull(buffer);
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
	public int compareTo(SafeString o) {
		return compare(getBytes(), o.getBytes());
	}

	private int compare(byte[] left, byte[] right) {
		for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
			int a = left[i] & 0xff;
			int b = right[j] & 0xff;
			if (a != b) {
				return a - b;
			}
		}
		return left.length - right.length;
	}

	@Override
	public int hashCode() {
		return Objects.hash(buffer);
	}

	@Override
	public boolean equals(Object obj) {
		return equalizer(this)
				.append((one, other) -> Objects.equals(one.buffer, other.buffer))
				.applyTo(obj);
	}

	@Override
	public String toString() {
		return DEFAULT_CHARSET.decode(buffer.duplicate()).toString();
	}

	public static SafeString safeString(String str) {
		return new SafeString(DEFAULT_CHARSET.encode(requireNonNull(str)));
	}

	public static List<SafeString> safeAsList(String... strs) {
		return Stream.of(requireNonNull(strs)).map(SafeString::safeString).collect(toList());
	}

	public static SafeString append(SafeString strA, SafeString strB) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(requireNonNull(strA).length() + requireNonNull(strB).length());
		byteBuffer.put(strA.getBytes());
		byteBuffer.put(strB.getBytes());
		byteBuffer.rewind();
		return new SafeString(byteBuffer);
	}
}

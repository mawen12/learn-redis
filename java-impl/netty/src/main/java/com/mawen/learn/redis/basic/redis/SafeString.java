package com.mawen.learn.redis.basic.redis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

	private final byte[] bytes;

	public SafeString(byte[] bytes) {
		Objects.requireNonNull(bytes);
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int length() {
		return bytes.length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
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
		if (!Arrays.equals(bytes, other.bytes)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return new String(bytes, DEFAULT_CHARSET);
	}

	public static SafeString safeString(String str) {
		Objects.requireNonNull(str);
		return new SafeString(str.getBytes(DEFAULT_CHARSET));
	}

	public static List<SafeString> safeAsList(String... strs) {
		Objects.requireNonNull(strs);
		return Stream.of(strs).map(SafeString::safeString).collect(toList());
	}
}

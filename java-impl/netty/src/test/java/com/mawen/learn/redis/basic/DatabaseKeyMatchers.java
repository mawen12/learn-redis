package com.mawen.learn.redis.basic;

import com.mawen.learn.redis.basic.data.DatabaseKey;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.mawen.learn.redis.basic.redis.SafeString.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class DatabaseKeyMatchers {

	public static DatabaseKey safeKey(String str) {
		return DatabaseKey.safeKey(safeString(str));
	}

	public static DatabaseKey safeKey(String str, long millis) {
		return DatabaseKey.safeKey(safeString(str), millis);
	}

	public static DatabaseKey safeKey(String str, int seconds) {
		return DatabaseKey.safeKey(safeString(str), seconds);
	}

	public static Matcher<DatabaseKey> isExpired() {
		return new KeyExpiredMatcher();
	}

	public static Matcher<DatabaseKey> isNotExpired() {
		return new KeyNotExpiredMatcher();
	}

	private static class KeyExpiredMatcher extends TypeSafeMatcher<DatabaseKey> {
		@Override
		protected boolean matchesSafely(DatabaseKey key) {
			return key.isExpired();
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("key is expired");
		}
	}

	private static class KeyNotExpiredMatcher extends TypeSafeMatcher<DatabaseKey> {
		@Override
		protected boolean matchesSafely(DatabaseKey key) {
			return !key.isExpired();
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("Key is not expired");
		}
	}
}

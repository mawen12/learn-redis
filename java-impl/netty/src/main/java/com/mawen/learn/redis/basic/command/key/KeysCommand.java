package com.mawen.learn.redis.basic.command.key;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.SafeString;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
@Command("keys")
@ParamLength(1)
public class KeysCommand implements ICommand {

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		Pattern pattern = createPattern(request.getParam(0));
		Predicate<? super String> predicate = key -> pattern.matcher(key).matches();
		Set<SafeString> keys = db.keySet().stream().filter(predicate).map(SafeString::safeString).collect(Collectors.toSet());
		response.addArray(keys);
	}

	private Pattern createPattern(String param) {
		return Pattern.compile(convertGlobToRegEX(param));
	}

	/**
	 * @see <a href="http://stackoverflow.com/questions/1247772/is-there-an-equivalent-of-java-util-regex-for-glob-type-patterns">Is there an equivalent of java.util.regex for "glob" type patterns?</a>
	 */
	private String convertGlobToRegEX(String line) {
		int strLen = line.length();
		StringBuilder sb = new StringBuilder(strLen);
		boolean escaping = false;
		int inCurlies = 0;
		for (char currentChar : line.toCharArray()) {
			switch (currentChar) {
				case '*':
					if (escaping) {
						sb.append("\\*");
					}
					else {
						sb.append(".*");
					}
					escaping = false;
					break;
				case '?':
					if (escaping) {
						sb.append("\\?");
					}
					else {
						sb.append(".");
					}
					escaping = false;
					break;
				case '.':
				case '(':
				case ')':
				case '+':
				case '|':
				case '^':
				case '$':
				case '@':
				case '%':
					sb.append('\\');
					sb.append(currentChar);
					escaping = false;
					break;
				case '\\':
					if (escaping) {
						sb.append("\\\\");
						escaping = false;
					}
					else {
						escaping = true;
					}
					break;
				case '{':
					if (escaping) {
						sb.append("\\{");
					}
					else {
						sb.append('(');
						inCurlies++;
					}
					escaping = false;
					break;
				case '}':
					if (inCurlies > 0 && !escaping) {
						sb.append(')');
						inCurlies--;
					}
					else if (escaping) {
						sb.append("\\}");
					}
					else {
						sb.append("}");
					}
					escaping = false;
					break;
				case ',':
					if (inCurlies > 0 && !escaping) {
						sb.append('|');
					}
					else if (escaping) {
						sb.append("\\,");
					}
					else {
						sb.append(",");
					}
					break;
				default:
					escaping = false;
					sb.append(currentChar);
			}
		}
		return sb.toString();
	}
}

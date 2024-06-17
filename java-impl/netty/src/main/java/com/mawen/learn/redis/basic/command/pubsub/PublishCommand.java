package com.mawen.learn.redis.basic.command.pubsub;

import java.util.Set;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.data.DatabaseKey;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@Command("publish")
@ParamLength(2)
public class PublishCommand implements ITinyDBCommand {

	private static final SafeString MESSAGE = safeString("message");

	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		IDatabase admin = getAdminDatabase(request.getServerContext());
		DatabaseValue value = getSubscriptions(admin, request.getParam(0));

		Set<SafeString> subscribers = value.getValue();
		for (SafeString subscriber : subscribers) {
			publish(request, subscriber);
		}

		response.addInt(subscribers.size());
	}

	private void publish(IRequest request, SafeString subscriber) {
		getTinyDB(request.getServerContext()).publish(subscriber.toString(), message(request));
	}

	private DatabaseValue getSubscriptions(IDatabase admin, SafeString channel) {
		DatabaseKey subscriptorsKey = safeKey(safeString(SUBSCRIPTIONS_PREFIX + channel));
		return admin.getOrDefault(subscriptorsKey, EMPTY_SET);
	}

	private RedisToken.ArrayRedisToken message(IRequest request) {
		return new RedisToken.ArrayRedisToken(asList(
				new RedisToken.StringRedisToken(MESSAGE),
				new RedisToken.StringRedisToken(request.getParam(0)),
				new RedisToken.StringRedisToken(request.getParam(1))
		));
	}
}

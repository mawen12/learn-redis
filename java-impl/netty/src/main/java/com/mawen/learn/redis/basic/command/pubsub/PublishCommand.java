package com.mawen.learn.redis.basic.command.pubsub;

import java.util.Set;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@Command("publish")
@ParamLength(2)
public class PublishCommand implements ICommand {

	private static final SafeString MESSAGE = safeString("message");

	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		IDatabase admin = request.getServerContext().getAdminDatabase();
		DatabaseValue value = admin.getOrDefault(safeKey(safeString(SUBSCRIPTIONS_PREFIX + request.getParam(0))), EMPTY_SET);

		Set<SafeString> subscribers = value.getValue();
		for (SafeString subscriber : subscribers) {
			request.getServerContext().publish(subscriber.toString(), message(request));
		}

		response.addInt(subscribers.size());
	}

	private String message(IRequest request) {
		Response stream = new Response();
		stream.addArray(asList(MESSAGE, request.getParam(0), request.getParam(1)));
		return stream.toString();
	}
}

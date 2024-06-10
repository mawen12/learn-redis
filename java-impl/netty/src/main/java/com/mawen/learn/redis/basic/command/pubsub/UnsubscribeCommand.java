package com.mawen.learn.redis.basic.command.pubsub;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.lang.String.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@Command("unsubscribe")
@ParamLength(1)
public class UnsubscribeCommand implements ICommand {

	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		int i = request.getLength();
		for (String channel : request.getParams()) {
			db.merge(SUBSCRIPTIONS_PREFIX + channel, set(request.getSession().getId()),(oldValue, newValue) -> {
				Set<String> merge = new HashSet<>();
				merge.addAll(oldValue.getValue());
				merge.remove(request.getSession().getId());
				return set(merge);
			});

			request.getSession().removeSubscription(channel);
			response.addArray(asList("unsubscribe", channel, valueOf(--i)));
		}
	}

}

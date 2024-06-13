package com.mawen.learn.redis.basic.command.pubsub;

import java.util.HashSet;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.command.annotation.PubSubAllowed;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;

import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@ReadOnly
@Command("subscribe")
@ParamLength(1)
@PubSubAllowed
public class SubscribeCommand implements ICommand {

	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		IDatabase admin = request.getServerContext().getAdminDatabase();
		int i = 1;
		for (String channel : request.getParams()) {
			admin.merge(SUBSCRIPTIONS_PREFIX + channel, set(request.getSession().getId()), (oldValue, newValue) -> {
				Set<String> merge = new HashSet<>();
				merge.addAll(oldValue.getValue());
				merge.add(request.getSession().getId());
				return set(merge);
			});

			request.getSession().addSubscription(channel);
			response.addArray(asList("subscribe", channel, i++));
		}
	}
}

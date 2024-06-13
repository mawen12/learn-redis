package com.mawen.learn.redis.basic.command.pubsub;

import java.util.Collection;
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
import com.mawen.learn.redis.basic.redis.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@ReadOnly
@Command("unsubscribe")
@ParamLength(1)
@PubSubAllowed
public class UnsubscribeCommand implements ICommand {

	private static final SafeString UNSUBSCRIBE = safeString("unsubscribe");
	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		IDatabase admin = request.getServerContext().getAdminDatabase();
		Collection<SafeString> channels = getChannels(request);
		int i = request.getLength();
		for (SafeString channel : channels) {
			admin.merge(safeKey(SUBSCRIPTIONS_PREFIX + channel), set(request.getSession().getId()),(oldValue, newValue) -> {
				Set<String> merge = new HashSet<>();
				merge.addAll(oldValue.getValue());
				merge.remove(request.getSession().getId());
				return set(merge);
			});

			request.getSession().removeSubscription(channel);
			response.addArray(asList(UNSUBSCRIBE, channel, --i));
		}
	}

	private Collection<SafeString> getChannels(IRequest request) {
		if (request.getParams().isEmpty()) {
			return request.getSession().getSubscriptions();
		}
		return request.getParams();
	}
}

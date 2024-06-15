package com.mawen.learn.redis.basic.command.pubsub;

import java.util.HashSet;
import java.util.Set;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.PubSubAllowed;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;
import com.mawen.learn.redis.resp.protocol.SafeString;

import static com.mawen.learn.redis.basic.data.DatabaseKey.*;
import static com.mawen.learn.redis.basic.data.DatabaseValue.*;
import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
@ReadOnly
@Command("subscribe")
@ParamLength(1)
@PubSubAllowed
public class SubscribeCommand implements ITinyDBCommand {

	private static final SafeString SUBSCRIBE = safeString("subscribe");

	private static final String SUBSCRIPTIONS_PREFIX = "subscriptions:";

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		IDatabase admin = getAdminDatabase(request.getServerContext());
		int i = 1;
		for (SafeString channel : request.getParams()) {
			admin.merge(safeKey(safeString(SUBSCRIPTIONS_PREFIX + channel)), set(safeString(request.getSession().getId())), (oldValue, newValue) -> {
				Set<SafeString> merge = new HashSet<>();
				merge.addAll(oldValue.getValue());
				merge.add(safeString(request.getSession().getId()));
				return set(merge);
			});

			getSessionState(request.getSession()).addSubscription(channel);
			response.addArray(asList(SUBSCRIBE, channel, i++));
		}
	}
}

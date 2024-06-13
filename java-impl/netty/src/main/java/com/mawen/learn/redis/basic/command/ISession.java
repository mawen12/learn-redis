package com.mawen.learn.redis.basic.command;

import java.util.Set;

import com.mawen.learn.redis.basic.redis.SafeString;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface ISession {

	String getId();

	ChannelHandlerContext getContext();

	Set<SafeString> getSubscriptions();

	void addSubscription(SafeString channel);

	void removeSubscription(SafeString channel);

	int getCurrentDB();

	void setCurrentDB(int db);

	void enqueue(Runnable task);

	void destroy();
}

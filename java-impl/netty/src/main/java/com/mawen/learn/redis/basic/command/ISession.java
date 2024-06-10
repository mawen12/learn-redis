package com.mawen.learn.redis.basic.command;

import java.util.Set;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/10
 */
public interface ISession {

	String getId();

	ChannelHandlerContext getContext();

	Set<String> getSubscriptions();

	void addSubscription(String channel);

	void removeSubscription(String channel);

	int getCurrentDB();

	void setCurrentDB(int db);
}

package com.mawen.learn.redis.resp.protocol;

import java.nio.ByteBuffer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public interface RedisSource {

	String readLine();

	ByteBuffer readBytes(int size);
}

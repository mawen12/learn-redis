package com.mawen.learn.redis.resp.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/7/12
 */
public class RedisEncoder extends MessageToByteEncoder<RedisToken> {

	@Override
	protected void encode(ChannelHandlerContext ctx, RedisToken msg, ByteBuf out) throws Exception {
		out.writeBytes(new RedisSerializer().encodedToken(msg));
	}
}

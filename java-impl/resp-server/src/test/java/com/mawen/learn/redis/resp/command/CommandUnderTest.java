package com.mawen.learn.redis.resp.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandUnderTest {

	Class<? extends ICommand> value();
}

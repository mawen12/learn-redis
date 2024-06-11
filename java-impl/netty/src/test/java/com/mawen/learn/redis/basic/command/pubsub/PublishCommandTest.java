package com.mawen.learn.redis.basic.command.pubsub;

import com.mawen.learn.redis.basic.command.CommandRule;
import com.mawen.learn.redis.basic.command.CommandUnderTest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

@CommandUnderTest(PublishCommand.class)
public class PublishCommandTest {

	@Rule
	public final CommandRule rule = new CommandRule(this);

	@Test
	public void testExecute() {
		rule.withData("subscriptions:test", DatabaseValue.set("localhost:12345"))
				.withParams("test", "Hello World!")
				.execute();

		rule.verify(IServerContext.class).publish("localhost:12345", "*3\r\n$7\r\nmessage\r\n$4\r\ntest\r\n$12\r\nHello World!\r\n");
		rule.verify(IResponse.class).addInt(1);
	}

}
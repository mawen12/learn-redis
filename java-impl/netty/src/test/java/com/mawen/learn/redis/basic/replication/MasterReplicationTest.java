package com.mawen.learn.redis.basic.replication;

import java.util.HashMap;

import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.RedisArray;
import com.mawen.learn.redis.basic.redis.RedisToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.basic.redis.SafeString.safeAsList;
import static com.mawen.learn.redis.basic.redis.SafeString.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MasterReplicationTest {

	@Mock
	private IServerContext server;

	@InjectMocks
	private MasterReplication master;

	private final IDatabase db = new Database(new HashMap<>());

	@Test
	public void testReplication() {
		when(server.getCommands()).thenReturn(asList(request()));
		when(server.getAdminDatabase()).thenReturn(db);

		master.addSlave("slave:1");
		master.addSlave("slave:2");

		master.start();

		verify(server, timeout(10000).times(2)).publish(anyString(), anyString());
	}

	private RedisArray request() {
		RedisArray array = new RedisArray();
		array.add(new RedisToken.StringRedisToken(safeString("set")));
		array.add(new RedisToken.StringRedisToken(safeString("a")));
		array.add(new RedisToken.StringRedisToken(safeString("b")));
		return array;
	}
}
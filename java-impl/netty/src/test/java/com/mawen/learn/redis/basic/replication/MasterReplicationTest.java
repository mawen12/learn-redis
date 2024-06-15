package com.mawen.learn.redis.basic.replication;

import java.util.ArrayList;
import java.util.List;

import com.mawen.learn.redis.basic.ITinyDB;
import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.resp.protocol.RedisToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mawen.learn.redis.resp.protocol.SafeString.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MasterReplicationTest {

	@Mock
	private ITinyDB server;

	@InjectMocks
	private MasterReplication master;

	private final IDatabase db = new Database();

	@Test
	public void testReplication() {
		when(server.getCommands()).thenReturn(asList(request()));
		when(server.getAdminDatabase()).thenReturn(db);

		master.addSlave("slave:1");
		master.addSlave("slave:2");

		master.start();

		verify(server, timeout(10000).times(2)).publish(anyString(), anyString());
	}

	private List<RedisToken> request() {
		List<RedisToken> array = new ArrayList<>();
		array.add(new RedisToken.StringRedisToken(safeString("set")));
		array.add(new RedisToken.StringRedisToken(safeString("a")));
		array.add(new RedisToken.StringRedisToken(safeString("b")));
		return array;
	}
}
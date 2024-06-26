package com.mawen.learn.redis.basic.command;

import com.mawen.learn.redis.basic.command.hash.HashDeleteCommand;
import com.mawen.learn.redis.basic.command.hash.HashExistsCommand;
import com.mawen.learn.redis.basic.command.hash.HashGetAllCommand;
import com.mawen.learn.redis.basic.command.hash.HashGetCommand;
import com.mawen.learn.redis.basic.command.hash.HashKeysCommand;
import com.mawen.learn.redis.basic.command.hash.HashLengthCommand;
import com.mawen.learn.redis.basic.command.hash.HashSetCommand;
import com.mawen.learn.redis.basic.command.hash.HashValuesCommand;
import com.mawen.learn.redis.basic.command.key.DeleteCommand;
import com.mawen.learn.redis.basic.command.key.ExistsCommand;
import com.mawen.learn.redis.basic.command.key.ExpireCommand;
import com.mawen.learn.redis.basic.command.key.KeysCommand;
import com.mawen.learn.redis.basic.command.key.PersistCommand;
import com.mawen.learn.redis.basic.command.key.RenameCommand;
import com.mawen.learn.redis.basic.command.key.TimeToLiveCommand;
import com.mawen.learn.redis.basic.command.key.TypeCommand;
import com.mawen.learn.redis.basic.command.list.LeftPopCommand;
import com.mawen.learn.redis.basic.command.list.LeftPushCommand;
import com.mawen.learn.redis.basic.command.list.ListIndexCommand;
import com.mawen.learn.redis.basic.command.list.ListLengthCommand;
import com.mawen.learn.redis.basic.command.list.ListRangeCommand;
import com.mawen.learn.redis.basic.command.list.ListSetCommand;
import com.mawen.learn.redis.basic.command.list.RightPopCommand;
import com.mawen.learn.redis.basic.command.list.RightPushCommand;
import com.mawen.learn.redis.basic.command.pubsub.PublishCommand;
import com.mawen.learn.redis.basic.command.pubsub.SubscribeCommand;
import com.mawen.learn.redis.basic.command.pubsub.UnsubscribeCommand;
import com.mawen.learn.redis.basic.command.server.FlushDBCommand;
import com.mawen.learn.redis.basic.command.server.InfoCommand;
import com.mawen.learn.redis.basic.command.server.SelectCommand;
import com.mawen.learn.redis.basic.command.server.SlaveOfCommand;
import com.mawen.learn.redis.basic.command.server.SyncCommand;
import com.mawen.learn.redis.basic.command.set.SetAddCommand;
import com.mawen.learn.redis.basic.command.set.SetCardinalityCommand;
import com.mawen.learn.redis.basic.command.set.SetDifferenceCommand;
import com.mawen.learn.redis.basic.command.set.SetIntersectionCommand;
import com.mawen.learn.redis.basic.command.set.SetIsMemberCommand;
import com.mawen.learn.redis.basic.command.set.SetMembersCommand;
import com.mawen.learn.redis.basic.command.set.SetRemoveCommand;
import com.mawen.learn.redis.basic.command.set.SetUnionCommand;
import com.mawen.learn.redis.basic.command.string.DecrementByCommand;
import com.mawen.learn.redis.basic.command.string.DecrementCommand;
import com.mawen.learn.redis.basic.command.string.GetCommand;
import com.mawen.learn.redis.basic.command.string.GetSetCommand;
import com.mawen.learn.redis.basic.command.string.IncrementByCommand;
import com.mawen.learn.redis.basic.command.string.IncrementCommand;
import com.mawen.learn.redis.basic.command.string.MultiGetCommand;
import com.mawen.learn.redis.basic.command.string.MultiSetCommand;
import com.mawen.learn.redis.basic.command.string.SetCommand;
import com.mawen.learn.redis.basic.command.string.SetExpiredCommand;
import com.mawen.learn.redis.basic.command.string.StringLengthCommand;
import com.mawen.learn.redis.basic.command.transaction.ExecCommand;
import com.mawen.learn.redis.basic.command.transaction.MultiCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetAddCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetCardinalityCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRangeByScoreCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRangeCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRemoveCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetReverseRangeCommand;
import com.mawen.learn.redis.resp.command.CommandSuite;
import com.mawen.learn.redis.resp.command.ICommand;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/14
 */
public class TinyDBCommandSuite extends CommandSuite {

	public TinyDBCommandSuite() {
		// connection
		addCommand(SelectCommand.class);
		addCommand(SyncCommand.class);
		addCommand(SlaveOfCommand.class);

		// server
		addCommand(FlushDBCommand.class);
		addCommand(InfoCommand.class);

		// strings
		addCommand(GetCommand.class);
		addCommand(MultiGetCommand.class);
		addCommand(SetCommand.class);
		addCommand(MultiSetCommand.class);
		addCommand(GetSetCommand.class);
		addCommand(IncrementCommand.class);
		addCommand(IncrementByCommand.class);
		addCommand(DecrementCommand.class);
		addCommand(DecrementByCommand.class);
		addCommand(StringLengthCommand.class);
		addCommand(SetExpiredCommand.class);

		// keys
		addCommand(DeleteCommand.class);
		addCommand(ExistsCommand.class);
		addCommand(TypeCommand.class);
		addCommand(RenameCommand.class);
		addCommand(KeysCommand.class);
		addCommand(ExpireCommand.class);
		addCommand(PersistCommand.class);
		addCommand(TimeToLiveCommand.class);

		// hash
		addCommand(HashSetCommand.class);
		addCommand(HashGetCommand.class);
		addCommand(HashGetAllCommand.class);
		addCommand(HashExistsCommand.class);
		addCommand(HashDeleteCommand.class);
		addCommand(HashKeysCommand.class);
		addCommand(HashLengthCommand.class);
		addCommand(HashValuesCommand.class);

		// list
		addCommand(LeftPushCommand.class);
		addCommand(LeftPopCommand.class);
		addCommand(RightPushCommand.class);
		addCommand(RightPopCommand.class);
		addCommand(ListLengthCommand.class);
		addCommand(ListRangeCommand.class);
		addCommand(ListIndexCommand.class);
		addCommand(ListSetCommand.class);

		// set
		addCommand(SetAddCommand.class);
		addCommand(SetMembersCommand.class);
		addCommand(SetCardinalityCommand.class);
		addCommand(SetIsMemberCommand.class);
		addCommand(SetRemoveCommand.class);
		addCommand(SetUnionCommand.class);
		addCommand(SetIntersectionCommand.class);
		addCommand(SetDifferenceCommand.class);

		// sorted set
		addCommand(SortedSetAddCommand.class);
		addCommand(SortedSetCardinalityCommand.class);
		addCommand(SortedSetRemoveCommand.class);
		addCommand(SortedSetRangeCommand.class);
		addCommand(SortedSetRangeByScoreCommand.class);
		addCommand(SortedSetReverseRangeCommand.class);

		// pub & sub
		addCommand(PublishCommand.class);
		addCommand(SubscribeCommand.class);
		addCommand(UnsubscribeCommand.class);

		// transactions
		addCommand(MultiCommand.class);
		addCommand(ExecCommand.class);
	}

	@Override
	protected ICommand wrap(Object command) {
		return new TinyDBCommandWrapper(command);
	}
}

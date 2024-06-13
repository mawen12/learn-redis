package com.mawen.learn.redis.basic.command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.command.hash.HashExistsCommand;
import com.mawen.learn.redis.basic.command.hash.HashGetAllCommand;
import com.mawen.learn.redis.basic.command.hash.HashGetCommand;
import com.mawen.learn.redis.basic.command.hash.HashKeysCommand;
import com.mawen.learn.redis.basic.command.hash.HashLengthCommand;
import com.mawen.learn.redis.basic.command.hash.HashSetCommand;
import com.mawen.learn.redis.basic.command.hash.HashValuesCommand;
import com.mawen.learn.redis.basic.command.key.DeleteCommand;
import com.mawen.learn.redis.basic.command.key.ExistsCommand;
import com.mawen.learn.redis.basic.command.key.KeysCommand;
import com.mawen.learn.redis.basic.command.key.RenameCommand;
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
import com.mawen.learn.redis.basic.command.server.EchoCommand;
import com.mawen.learn.redis.basic.command.server.FlushDBCommand;
import com.mawen.learn.redis.basic.command.server.InfoCommand;
import com.mawen.learn.redis.basic.command.server.PingCommand;
import com.mawen.learn.redis.basic.command.server.QuitCommand;
import com.mawen.learn.redis.basic.command.server.SelectCommand;
import com.mawen.learn.redis.basic.command.server.TimeCommand;
import com.mawen.learn.redis.basic.command.set.SetAddCommand;
import com.mawen.learn.redis.basic.command.set.SetCardinalityCommand;
import com.mawen.learn.redis.basic.command.set.SetDifferenceCommand;
import com.mawen.learn.redis.basic.command.set.SetIntersectionCommand;
import com.mawen.learn.redis.basic.command.set.SetIsMemberCommand;
import com.mawen.learn.redis.basic.command.set.SetMembersCommand;
import com.mawen.learn.redis.basic.command.set.SetPopCommand;
import com.mawen.learn.redis.basic.command.set.SetRandomMemberCommand;
import com.mawen.learn.redis.basic.command.set.SetRemoveCommand;
import com.mawen.learn.redis.basic.command.set.SetUnionCommand;
import com.mawen.learn.redis.basic.command.string.DecrementByCommand;
import com.mawen.learn.redis.basic.command.string.DecrementCommand;
import com.mawen.learn.redis.basic.command.string.GetCommand;
import com.mawen.learn.redis.basic.command.string.GetSetCommand;
import com.mawen.learn.redis.basic.command.string.IncrementByCommand;
import com.mawen.learn.redis.basic.command.string.IncrementCommand;
import com.mawen.learn.redis.basic.command.string.MultiSetCommand;
import com.mawen.learn.redis.basic.command.string.SetCommand;
import com.mawen.learn.redis.basic.command.string.StringLengthCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetAddCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetCardinalityCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRangeByScoreCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRangeCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetRemoveCommand;
import com.mawen.learn.redis.basic.command.zset.SortedSetReverseRangeCommand;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
public class CommandSuite {

	private static final Logger logger = Logger.getLogger(CommandSuite.class.getName());

	private final Map<String, Class<? extends ICommand>> metadata = new HashMap<>();
	private final Map<String, ICommand> commands = new HashMap<>();

	public CommandSuite() {
		// connection
		addCommand(PingCommand.class);
		addCommand(EchoCommand.class);

		// server
		addCommand(FlushDBCommand.class);
		addCommand(TimeCommand.class);
		addCommand(SelectCommand.class);
		addCommand(InfoCommand.class);
		addCommand(QuitCommand.class);

		// strings
		addCommand(GetCommand.class);
		addCommand(SetCommand.class);
		addCommand(SetCommand.class);
		addCommand(MultiSetCommand.class);
		addCommand(GetSetCommand.class);
		addCommand(IncrementCommand.class);
		addCommand(IncrementByCommand.class);
		addCommand(DecrementByCommand.class);
		addCommand(DecrementCommand.class);
		addCommand(StringLengthCommand.class);

		// keys
		addCommand(DeleteCommand.class);
		addCommand(ExistsCommand.class);
		addCommand(TypeCommand.class);
		addCommand(RenameCommand.class);
		addCommand(KeysCommand.class);

		// hash
		addCommand(HashSetCommand.class);
		addCommand(HashGetCommand.class);
		addCommand(HashGetAllCommand.class);
		addCommand(HashExistsCommand.class);
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
		addCommand(SetPopCommand.class);
		addCommand(SetRandomMemberCommand.class);

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
	}

	private void addCommand(Class<? extends ICommand> clazz) {
		try {
			ICommand command = clazz.newInstance();

			Command annotation = clazz.getAnnotation(Command.class);

			if (annotation != null) {
				commands.put(annotation.value(), command(command));
				metadata.put(annotation.value(), clazz);
			}
			else {
				logger.warning(() -> "annotation not present at " + clazz.getName());
			}
		}
		catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "error loading command: " + clazz.getName(), e);
		}
	}

	private ICommand command(ICommand command) {
		return new CommandWrapper(command);
	}

	public ICommand getCommand(String name) {
		return commands.get(name.toLowerCase());
	}

	public boolean isReadOnlyCommand(String command) {
		Class<? extends ICommand> clazz = metadata.get(command);
		if (clazz != null) {
			return clazz.isAnnotationPresent(ReadOnly.class);
		}
		return true;
	}
}

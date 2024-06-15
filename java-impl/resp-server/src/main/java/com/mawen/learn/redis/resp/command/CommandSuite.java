package com.mawen.learn.redis.resp.command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.command.server.EchoCommand;
import com.mawen.learn.redis.resp.command.server.PingCommand;
import com.mawen.learn.redis.resp.command.server.QuitCommand;
import com.mawen.learn.redis.resp.command.server.TimeCommand;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/8
 */
public class CommandSuite {

	private static final Logger logger = Logger.getLogger(CommandSuite.class.getName());

	private final Map<String, ICommand> commands = new HashMap<>();

	private final NullCommand nullCommand = new NullCommand();

	public CommandSuite() {
		addCommand(PingCommand.class);
		addCommand(EchoCommand.class);
		addCommand(TimeCommand.class);
		addCommand(QuitCommand.class);
	}

	protected void addCommand(Class<? extends ICommand> clazz) {
		try {
			ICommand command = clazz.newInstance();

			Command annotation = clazz.getAnnotation(Command.class);

			if (annotation != null) {
				commands.put(annotation.value(), wrap(command));
			}
			else {
				logger.warning(() -> "annotation not present at " + clazz.getName());
			}
		}
		catch (InstantiationException | IllegalAccessException e) {
			logger.log(Level.SEVERE, "error loading command: " + clazz.getName(), e);
		}
	}

	protected ICommand wrap(Object command) {
		if (command instanceof ICommand) {
			return new CommandWrapper((ICommand) command);
		}
		throw new RuntimeException();
	}

	public ICommand getCommand(String name) {
		return commands.getOrDefault(name.toLowerCase(), nullCommand);
	}
}
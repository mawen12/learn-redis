package com.mawen.learn.redis.resp.command;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CommandSuiteTest {

	private final CommandSuite commandSuite = new CommandSuite();

	@Test
	public void getCommandNull() {
		ICommand command = commandSuite.getCommand("notExists");

		assertThat(command, is(instanceOf(NullCommand.class)));
	}

}
package com.mawen.learn.redis.basic.command.impl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import com.mawen.learn.redis.basic.command.CommandWrapper;
import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
public class CommandRule implements TestRule {

	private IRequest request;

	private IResponse response;

	private IDatabase database;

	private final Object target;

	public CommandRule(Object target) {
		super();
		this.target = target;
	}

	public IRequest getRequest() {
		return request;
	}

	public IResponse getResponse() {
		return response;
	}

	public IDatabase getDatabase() {
		return database;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				request = mock(IRequest.class);
				response = mock(IResponse.class);
				database = new Database(new HashMap<>());

				MockitoAnnotations.initMocks(target);

				base.evaluate();

				database.clear();
			}
		};
	}

	public CommandRule execute(ICommand command) {
		new CommandWrapper(command).execute(database,request,  response);
		return this;
	}

	public CommandRule withParams(String... params) {
		if (params != null) {
			when(request.getParams()).thenReturn(Arrays.asList(params));
			int i = 0;
			for (String param : params) {
				when(request.getParam(i++)).thenReturn(param);
			}
			when(request.getLength()).thenReturn(params.length);
		}
		return this;
	}

}
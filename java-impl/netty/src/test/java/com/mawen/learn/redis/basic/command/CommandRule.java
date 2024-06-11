package com.mawen.learn.redis.basic.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.mawen.learn.redis.basic.data.Database;
import com.mawen.learn.redis.basic.data.DatabaseValue;
import com.mawen.learn.redis.basic.data.IDatabase;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/7
 */
public class CommandRule implements TestRule {

	private IRequest request;

	private IResponse response;

	private IDatabase database;

	private IServerContext server;

	private ISession session;

	private final Object target;

	private ICommand command;

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
	public Statement apply(Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				server = mock(IServerContext.class);
				request = mock(IRequest.class);
				response = mock(IResponse.class);
				session = mock(ISession.class);
				database = new Database(new HashMap<>());

				when(request.getServerContext()).thenReturn(server);
				when(request.getSession()).thenReturn(session);
				when(session.getId()).thenReturn("localhost:12345");
				when(server.getDatabase()).thenReturn(database);

				MockitoAnnotations.initMocks(target);

				command = target.getClass().getAnnotation(CommandUnderTest.class).value().newInstance();

				base.evaluate();

				database.clear();
			}
		};
	}

	public CommandRule execute() {
		reset(response);
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
			when(request.getOptionalParam(anyInt())).thenAnswer(new Answer<Optional<String>>() {
				@Override
				public Optional<String> answer(InvocationOnMock invocation) throws Throwable {
					Integer i = (Integer) invocation.getArguments()[0];
					if (i < params.length) {
						return Optional.of(params[i]);
					}
					return Optional.empty();
				}
			});
		}
		return this;
	}

	public CommandRule withData(String key, DatabaseValue value) {
		database.put(key, value);
		return this;
	}

	public CommandRule assertThat(String key, Matcher<DatabaseValue> matcher) {
		Assert.assertThat(database.get(key), matcher);
		return this;
	}

	public IResponse verify() {
		return Mockito.verify(response);
	}

	@SuppressWarnings("unchecked")
	public <T> T verify(Class<T> type) {
		if (type.equals(IServerContext.class)) {
			return (T) Mockito.verify(server);
		}
		else if (type.equals(ISession.class)) {
			return (T) Mockito.verify(session);
		}
		else {
			return (T) verify();
		}
	}
}

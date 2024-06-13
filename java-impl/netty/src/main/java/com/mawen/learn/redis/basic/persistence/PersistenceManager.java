package com.mawen.learn.redis.basic.persistence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mawen.learn.redis.basic.TinyDBConfig;
import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.ISession;
import com.mawen.learn.redis.basic.command.Request;
import com.mawen.learn.redis.basic.command.Response;
import com.mawen.learn.redis.basic.command.Session;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.RedisArray;
import com.mawen.learn.redis.basic.redis.RedisParser;
import com.mawen.learn.redis.basic.redis.RedisSource;
import com.mawen.learn.redis.basic.redis.RedisToken;
import com.mawen.learn.redis.basic.redis.SafeString;

import static java.util.stream.Collectors.*;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/13
 */
public class PersistenceManager implements Runnable {

	private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());

	private OutputStream output;

	private IServerContext server;

	private String dumpFile;
	private String redoFile;

	private int syncPeriod;

	private ISession session = new Session("dummy", null);

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public PersistenceManager(IServerContext server, TinyDBConfig config) {
		this.server = server;
		this.dumpFile = config.getRdbFile();
		this.redoFile = config.getAofFile();
		this.syncPeriod = config.getSyncPeriod();
	}

	public void start() {
		importRDB();
		importRedo();
		createRedo();
		executor.scheduleWithFixedDelay(this, syncPeriod, syncPeriod, TimeUnit.SECONDS);
		logger.info(() -> "Persistence manager started");
	}

	public void stop() {
		executor.shutdown();
		closeRedo();
		exportRDB();
		logger.info(() -> "Persistence manager stopped");
	}

	@Override
	public void run() {
		exportRDB();
		createRedo();
	}

	public void append(RedisArray command) {
		if (output != null) {
			executor.submit(() -> appendRedo(command));
		}
	}

	private void importRDB() {
		File file = new File(dumpFile);
		if (file.exists()) {
			try (InputStream rdb = new FileInputStream(file)) {
				server.importRDB(rdb);
				logger.info(() -> "RDB file imported");
			}
			catch (IOException e) {
				logger.log(Level.SEVERE, "error reading RDB, e");
			}
		}
	}

	private void importRedo() {
		File file = new File(redoFile);
		if (file.exists()) {
			try (FileInputStream redo = new FileInputStream(file)) {
				RedisParser parser = new RedisParser(new InputStreamRedisSource(redo));

				while (true) {
					RedisToken token = parser.parse();
					if (token == null) {
						break;
					}
					processCommand(token);
				}
			}
			catch (IOException e) {
				logger.log(Level.SEVERE, "error reading RDB", e);
			}
		}
	}

	private void processCommand(RedisToken token) {
		RedisArray array = token.getValue();

		RedisToken commandToken = array.remove(0);

		logger.fine(() -> "command received from master: " + commandToken.getValue());

		ICommand command = server.getCommand(commandToken.getValue().toString());

		if (command != null) {
			IDatabase current = server.getDatabase(session.getCurrentDB());
			command.execute(current, request(token, array), new Response());
		}
	}

	private Request request(RedisToken commandToken, RedisArray array) {
		return new Request(server, session, commandToken.getValue(), arrayToList(array));
	}

	private List<SafeString> arrayToList(RedisArray array) {
		List<SafeString> cmd = new LinkedList<>();
		for (RedisToken token : array) {
			cmd.add(token.getValue());
		}
		return cmd;
	}

	private void createRedo() {
		try {
			closeRedo();
			output = new FileOutputStream(redoFile);
			logger.info(() -> "AOF file created");
		}
		catch (IOException e) {
			throw new IOError(e);
		}
	}

	private void closeRedo() {
		try {
			if (output != null) {
				output.close();
				output = null;
				logger.fine(() -> "AOF file closed");
			}
		}
		catch (IOException e) {
			logger.severe("error closing file");
		}
	}

	private void exportRDB() {
		try (FileOutputStream rdb = new FileOutputStream(dumpFile)) {
			server.exportRDB(rdb);
			logger.info(() -> "RDB file exported");
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "error writing to RDB file", e);
		}
	}

	private void appendRedo(RedisArray command) {
		try {
			Response response = new Response();
			response.addArray(command.stream().map(RedisToken::getValue).collect(toList()));
			output.write(response.getBytes());
			output.flush();
			logger.fine(() -> "new command: " + command);
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "error writing to AOF file", e);
		}
	}

	private static class InputStreamRedisSource implements RedisSource {

		private InputStream stream;

		public InputStreamRedisSource(InputStream stream) {
			super();
			this.stream = stream;
		}

		@Override
		public String readLine() {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				boolean cr = false;
				while (true) {
					int read = stream.read();

					if (read == -1) {
						// end of stream
						break;
					}

					if (read == '\r') {
						cr = true;
					}
					else if (cr && read == '\n') {
						break;
					}
					else {
						cr = false;

						baos.write(read);
					}
				}
				return baos.toString("UTF-8");
			}
			catch (IOException e) {
				throw new IOError(e);
			}
		}

		@Override
		public ByteBuffer readBytes(int size) {
			try {
				byte[] buffer = new byte[size];
				int readed = stream.read(buffer);
				if (readed > -1) {
					return ByteBuffer.wrap(buffer, 0, readed);
				}
				return null;
			}
			catch (IOException e) {
				throw new IOError(e);
			}
		}
	}
}

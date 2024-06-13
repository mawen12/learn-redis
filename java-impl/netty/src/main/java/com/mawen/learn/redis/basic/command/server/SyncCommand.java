package com.mawen.learn.redis.basic.command.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.IServerContext;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.redis.SafeString;
import com.mawen.learn.redis.basic.replication.MasterReplication;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
@ReadOnly
@Command("sync")
public class SyncCommand implements ICommand {

	private MasterReplication master;

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		try {
			IServerContext server = request.getServerContext();

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			server.exportRDB(output);

			response.addBulkStr(new SafeString(output.toByteArray()));

			if (master == null) {
				master = new MasterReplication(server);
				master.start();
			}

			master.addSlave(request.getSession().getId());
		}
		catch (IOException e) {
			response.addError("ERROR replication error");
		}
	}
}

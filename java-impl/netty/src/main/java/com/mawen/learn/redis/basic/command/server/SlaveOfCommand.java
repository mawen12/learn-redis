package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ICommand;
import com.mawen.learn.redis.basic.command.IRequest;
import com.mawen.learn.redis.basic.command.IResponse;
import com.mawen.learn.redis.basic.command.annotation.Command;
import com.mawen.learn.redis.basic.command.annotation.ParamLength;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.replication.SlaveReplication;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
@Command("slaveof")
@ParamLength(2)
public class SlaveOfCommand implements ICommand {

	private SlaveReplication slave;

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		String host = request.getParam(0);
		String port = request.getParam(1);

		boolean stopCurrent = host.equals("NO") && port.equals("ONE");

		if (slave == null) {
			if (!stopCurrent) {
				startReplication(request, host, port);
			}
		}
		else {
			slave.stop();

			if (!stopCurrent) {
				startReplication(request, host, port);
			}
		}

		response.addSimpleStr(RESULT_OK);
	}

	private void startReplication(IRequest request, String host, String port) {
		slave = new SlaveReplication(request.getServerContext(), request.getSession(), host, Integer.parseInt(port));
		slave.start();
	}
}

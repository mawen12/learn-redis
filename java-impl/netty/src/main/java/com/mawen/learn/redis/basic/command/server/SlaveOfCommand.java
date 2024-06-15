package com.mawen.learn.redis.basic.command.server;

import com.mawen.learn.redis.basic.command.ITinyDBCommand;
import com.mawen.learn.redis.basic.command.annotation.ReadOnly;
import com.mawen.learn.redis.basic.data.IDatabase;
import com.mawen.learn.redis.basic.replication.SlaveReplication;
import com.mawen.learn.redis.resp.annotation.Command;
import com.mawen.learn.redis.resp.annotation.ParamLength;
import com.mawen.learn.redis.resp.command.IRequest;
import com.mawen.learn.redis.resp.command.IResponse;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/12
 */
@ReadOnly
@Command("slaveof")
@ParamLength(2)
public class SlaveOfCommand implements ITinyDBCommand {

	private SlaveReplication slave;

	@Override
	public void execute(IDatabase db, IRequest request, IResponse response) {
		String host = request.getParam(0).toString();
		String port = request.getParam(1).toString();

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

		response.addSimpleStr(IResponse.RESULT_OK);
	}

	private void startReplication(IRequest request, String host, String port) {
		slave = new SlaveReplication(getTinyDB(request.getServerContext()), request.getSession(), host, Integer.parseInt(port));
		slave.start();
	}
}

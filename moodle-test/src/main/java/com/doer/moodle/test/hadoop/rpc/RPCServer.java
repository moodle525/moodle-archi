package com.doer.moodle.test.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

public class RPCServer implements Bizable {

	@Override
	public String sayHi(String name) {
		return "hi " + name;
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Server server = new RPC.Builder(conf).setProtocol(Bizable.class).setInstance(new RPCServer())
				.setBindAddress("127.0.0.1").setPort(9527).build();
		server.start();
	}

}

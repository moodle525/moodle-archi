package com.doer.moodle.test.hadoop.rpc;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

public class RPCClient {
	public static void main(String[] args) throws Exception{
		Bizable proxy = RPC.getProxy(Bizable.class, 10020, new InetSocketAddress("127.0.0.1", 9527), new Configuration());
		String re = proxy.sayHi("tom");
		System.out.println(re);
	}
}

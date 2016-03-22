package com.doer.moodle.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.config.zk.ZkClient;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "config.xml" });
		ZkClient zkClient = (ZkClient) ctx.getBean("zkClient");
		System.out.println(zkClient);
		String pa = "/git/ssss";
		zkClient.forPath(pa,"s");
		System.out.println(zkClient.getData(pa));
		//ss();
	}
	
	public static void ss() throws Exception{
		 final String PATH = "/as"; 
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("10.211.55.8:2181")  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .build();  
		client.start(); 
		client.create().forPath(PATH, "I love messi".getBytes());  
		  
        byte[] bs = client.getData().forPath(PATH);  
        System.out.println("新建的节点，data为:" + new String(bs));
	}
}

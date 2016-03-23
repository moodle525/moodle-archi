package com.doer.moodle.config.zk;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.common.contants.ConfigConstant;
import com.google.gson.Gson;

@SuppressWarnings("resource")
public class ZkUtil {
	private ZkUtil() {
	}

	static ZkClient zkClient;
	
	static {
		System.out.println("zkClient utils...............");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "config.xml" });
		zkClient = (ZkClient) ctx.getBean("zkClient");

	}

	public static String getConfig(String path) {
		return zkClient.getData(path);
	}
	
	public static List<String> getChildren(String path){
		return zkClient.getChildren(path);
	}
	
	public static void main(String[] args) {
		List<String> s = ZkUtil.getChildren(ConfigConstant.CONFIG_INFO_PATH);
		System.out.println(new Gson().toJson(s));
	}
}

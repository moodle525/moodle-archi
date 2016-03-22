package com.doer.moodle.config.zk;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.config.ConfigurationCenter;

@SuppressWarnings("resource")
public class ZkUtil {
	private ZkUtil() {
	}

	static ConfigurationCenter configCenter;
	
	static {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "config.xml" });
		configCenter = (ConfigurationCenter) ctx.getBean("configCenter");

	}

	public static String getConfig(String path) {
		return configCenter.getConfig(path);
	}
	
	public static void main(String[] args) {
		String ss = ZkUtil.getConfig("/com/doer/moodle/jdbc");
		System.out.println(ss);
	}
}

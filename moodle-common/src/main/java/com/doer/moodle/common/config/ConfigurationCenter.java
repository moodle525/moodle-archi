package com.doer.moodle.common.config;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.common.config.zk.ZkClient;
import com.doer.moodle.common.contants.ConfigConstant;

public class ConfigurationCenter {
	private ZkClient zkClient;
	private List<String> configFiles;
	private boolean initWriteData;
	private static final Logger log = Logger.getLogger(ConfigurationCenter.class);

	public ConfigurationCenter() {
	}

	public void init() {
		Properties props = new Properties();
		try {
			for (String configFile : configFiles) {
				props.load(this.getClass().getResourceAsStream(configFile));
			}
			if (initWriteData) {
				writeData(props);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeData(Properties props) {
		Set<Object> keyValue = props.keySet();
		for (Iterator<Object> it = keyValue.iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = (String) props.getProperty(key);
			String subPath = key.replace(ConfigConstant.UNIX_SEPERATE, ConfigConstant.DOT).substring(1, key.length());
			zkClient.create(ConfigConstant.CONFIG_INFO_PATH + ConfigConstant.UNIX_SEPERATE + subPath, "");
			zkClient.create(key, value);
		}
	}

	public String getConfig(String path) {
		return zkClient.getData(path);
	}

	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	public List<String> getConfigFiles() {
		return configFiles;
	}

	public void setConfigFiles(List<String> configFiles) {
		this.configFiles = configFiles;
	}

	public boolean isInitWriteData() {
		return initWriteData;
	}

	public void setInitWriteData(boolean initWriteData) {
		this.initWriteData = initWriteData;
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "config.xml" });
		ConfigurationCenter confCenter = (ConfigurationCenter) ctx.getBean("configCenter");
		log.info(confCenter.getConfig("/com/doer/moodle/dubbo"));
	}
}

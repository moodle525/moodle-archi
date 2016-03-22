package com.doer.moodle.config;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.config.zk.ZkClient;

public class ConfigurationCenter {
	private ZkClient zkClient;
	private List<String> configFiles;
	private boolean initWriteData;
	private static final Logger log = Logger
			.getLogger(ConfigurationCenter.class);

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
			String path = (String) it.next();
			String data = (String) props.getProperty(path);
			zkClient.forPath(path, data);
			log.info("写入配置信息【" + path + ":" + data + "】");
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
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "config.xml" });
	}
}

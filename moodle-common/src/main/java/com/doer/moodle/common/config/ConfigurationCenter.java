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

/**
 * 配置中心，加载配置信息
 * @author lixiongcheng
 *
 */
public class ConfigurationCenter {
	/**
	 * ZkClient
	 */
	private ZkClient zkClient;
	/**
	 * 配置文件
	 */
	private List<String> configFiles;
	/**
	 * 初始化时是否重新加载配置
	 */
	private boolean refresh;
	private static final Logger log = Logger.getLogger(ConfigurationCenter.class);

	public ConfigurationCenter() {
	}

	public void init() {
		if (refresh) {
			writeConfig(loadConfig());
			log.info("config load successfully...");
		}

	}

	public Properties loadConfig() {
		Properties props = new Properties();
		try {
			for (String configFile : configFiles) {
				props.load(this.getClass().getResourceAsStream(configFile));
			}
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		return props;
	}

	public void writeConfig(Properties props) {
		Set<Object> keyValue = props.keySet();
		for (Iterator<Object> it = keyValue.iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = (String) props.getProperty(key);
			String subPath = key.replace(ConfigConstant.UNIX_SEPERATE, ConfigConstant.DOT).substring(1, key.length());
			//保存配置key
			zkClient.create(ConfigConstant.CONFIG_INFO_PATH + ConfigConstant.UNIX_SEPERATE + subPath, "");
			//保存配置信息key-value
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

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "config.xml" });
		ConfigurationCenter confCenter = (ConfigurationCenter) ctx.getBean("configCenter");
		log.info(confCenter.getConfig("/com/doer/moodle/dubbo"));
	}
}

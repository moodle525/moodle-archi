package com.doer.moodle.config.zk;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.common.contants.ConfigConstant;
import com.doer.moodle.common.exceptions.PlatformException;
import com.google.gson.Gson;

public class ZkClient {
	private static final Logger log = Logger.getLogger(ZkClient.class);

	private CuratorFramework curator;
	private String zkAddress;
	private int timeOut;
	private int sessionTimeOut;

	public ZkClient() {
	}

	public void init() {
		if (StringUtils.isBlank(zkAddress))
			throw new PlatformException("0", "zk address is not be null");
		if (sessionTimeOut <= 0)
			sessionTimeOut = 2000;
		if (timeOut <= 0)
			timeOut = 2000;
		curator = CuratorFrameworkFactory.builder().connectString(zkAddress).sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(timeOut).canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)).build();
		curator.start();
	}

	public void close() {
		curator.close();
	}

	public boolean exists(String path) {
		Stat stat = null;
		try {
			stat = curator.checkExists().forPath(path);
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		return stat == null ? false : true;
	}

	public String delete(final String path) {
		try {
			curator.delete().inBackground().forPath(path);
		} catch (Exception e) {
		}
		return path;
	}

	public List<String> getChildren(String path) {
		List<String> children = null;
		try {
			children = curator.getChildren().forPath(path);
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		return children;
	}

	public String forPath(String path, String data) {
		String _path = null;
		try {
			if (!exists(path)) {
				_path = curator.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
			}
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		return _path;
	}

	public String forPath(String path) {
		return forPath(path, "");
	}

	public String getData(String path) {
		byte[] data = null;
		try {
			data = curator.getData().forPath(path);
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		if (data == null)
			return null;
		else
			return new String(data);
	}

	public void setData(String path, String data) {
		try {
			curator.setData().forPath(path, data.getBytes());
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
	}

	public String getZkAddress() {
		return zkAddress;
	}

	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	public CuratorFramework getCurator() {
		return curator;
	}

	public void setCurator(CuratorFramework curator) {
		this.curator = curator;
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "config.xml" });
		ZkClient zkClient = (ZkClient) ctx.getBean("zkClient");
		System.out.println(zkClient.exists(ConfigConstant.CONFIG_INFO_PATH));
		List<String> children = zkClient.getChildren("/com/doer/moodle");
		System.out.println(new Gson().toJson(children));
	}

}

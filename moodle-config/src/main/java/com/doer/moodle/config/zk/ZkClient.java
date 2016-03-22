package com.doer.moodle.config.zk;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.doer.moodle.common.exceptions.PlatformException;

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
		curator = CuratorFrameworkFactory
				.builder()
				.connectString(zkAddress)
				.sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(timeOut)
				.canBeReadOnly(false)
				.retryPolicy(
						new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.build();
		curator.start();
	}

	public void close() {
		curator.close();
	}

	public String forPath(String path, String data) {
		String _path = null;
		try {
			_path = curator.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
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

	public void delete(String path) {
		curator.delete();
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

}

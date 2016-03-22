package com.doer.moodle.config.zk;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.doer.moodle.common.exceptions.PlatformException;

public class ZkClient {
	private CuratorFramework curator;
	private String zkAddress;
	private int timeOut;
	private int sessionTimeOut;

	public ZkClient(String zkAddess) throws Exception {
		this(zkAddess, 0, 0, null);
		this.zkAddress = zkAddess;
	}

	public ZkClient(String zkAddress, int sessionTimeOut, int timeOut) throws Exception {
		this(zkAddress, sessionTimeOut, timeOut, null);
		this.zkAddress = zkAddress;
		this.sessionTimeOut = sessionTimeOut;
		this.timeOut = timeOut;
	}

	public ZkClient(String zkAddress, int sessionTimeOut, int timeOut, RetryPolicy retryPolicy) throws Exception {
		if (StringUtils.isBlank(zkAddress))
			throw new PlatformException("0", "zk address is not be null");
		this.zkAddress = zkAddress;
		if (sessionTimeOut <= 0)
			sessionTimeOut = 2000;
		this.sessionTimeOut = sessionTimeOut;
		if (timeOut <= 0)
			timeOut = 2000;
		this.timeOut = timeOut;
		if (retryPolicy == null)
			retryPolicy = new ExponentialBackoffRetry(1000, 3);
		curator = CuratorFrameworkFactory.newClient(zkAddress, sessionTimeOut, timeOut, retryPolicy);
		curator.start();
	}
	
	public String forPath(String path,byte[] data) throws Exception{
		return curator.create().forPath(path, data);
	}
	
	public String forPath(String path) throws Exception{
		return forPath(path,"");
	}
	
	public String forPath(String path,String data) throws Exception{
		return forPath(path,data.getBytes());
	}
	
	public void delete(String path){
		curator.delete();
	}

	public void close() {
		curator.close();
	}

	public CuratorFramework getCurator() {
		return curator;
	}

	public void setCurator(CuratorFramework curator) {
		this.curator = curator;
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

}

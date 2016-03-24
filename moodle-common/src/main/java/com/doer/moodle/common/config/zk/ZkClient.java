package com.doer.moodle.common.config.zk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.common.contants.ConfigConstant;
import com.doer.moodle.common.exceptions.PlatformException;
import com.doer.moodle.common.util.Base64Util;

/**
 * ZooKeeper操作客户端
 * 
 * @author lixiongcheng
 *
 */
public class ZkClient {
	private static final Logger log = Logger.getLogger(ZkClient.class);

	private static CuratorFramework client;
	private String zkAddress;
	private int timeOut;
	private int sessionTimeOut;
	private String authInfo;

	public ZkClient() {
	}

	public void init() throws Exception {
		if (StringUtils.isBlank(zkAddress))
			throw new PlatformException("0", "zk address can not be null");
		if (StringUtils.isBlank(authInfo) || "null".equalsIgnoreCase(authInfo))
			throw new PlatformException("0", "authInfo can not be null or null char");
		if (sessionTimeOut <= 0)
			sessionTimeOut = 2000;
		if (timeOut <= 0)
			timeOut = 2000;
		client = CuratorFrameworkFactory.builder().connectString(zkAddress).sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(timeOut).canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.authorization(ConfigConstant.AuthType.DIGEST, authInfo.getBytes()).build();
		client.start();
	}

	public void close() {
		client.close();
	}

	public boolean exists(String path) {
		Stat stat = null;
		try {
			stat = client.checkExists().forPath(path);
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		return stat == null ? false : true;
	}

	/**
	 * 删除节点，及其父节点。
	 * 
	 * @param path
	 * @return
	 */
	public String delete(final String path) {
		try {
			//// 保证节点必须删除，如果删除出现错误，则后台程序会不断去尝试删除。
			client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			throw new PlatformException("0", e);
		}
		return path;
	}

	public List<String> getChildren(String path) {
		List<String> children = null;
		try {
			children = client.getChildren().forPath(path);
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
		return children;
	}

	public CuratorTransaction inTransaction() {
		return client.inTransaction();
	}

	private List<ACL> setBasicAcl() {
		List<ACL> acls = new ArrayList<ACL>();
		try {
			acls.add(new ACL(Perms.ALL,
					new Id(ConfigConstant.AuthType.DIGEST, DigestAuthenticationProvider.generateDigest(authInfo))));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return acls;
	}

	/**
	 * 创建永久节点
	 * 
	 * @param path
	 * @param data
	 */
	public void create(String path, String data) {
		if (data == null)
			data = "";
		try {

			if (!exists(path)) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(setBasicAcl())
						.forPath(path, data.getBytes());
				log.info("node[" + path + "]created," + data);
			} else {
				String dataed = getData(path);
				if (!StringUtils.isEmpty(dataed) && !dataed.equals(data)) {
					client.setACL().withACL(setBasicAcl());
					setData(path, data);
					log.info("node[" + path + "]updated," + data);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
	}

	/**
	 * 创建临时节点，CreateMode.EPHEMERAL
	 * 
	 * @param path
	 * @param data
	 */
	public void createEphemeral(String path, String data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
	}

	public void createPersistentSequential(String path, String data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path,
					data.getBytes());
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
	}

	public void createEphemeralSequential(String path, String data) {
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,
					data.getBytes());
		} catch (Exception e) {
			log.error(e);
			throw new PlatformException("0", e);
		}
	}

	public String getData(String path) {
		byte[] data = null;
		try {
			data = client.getData().forPath(path);
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
			client.setData().forPath(path, data.getBytes());
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

	public String getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}

	static public String generateDigest(String idPassword) throws NoSuchAlgorithmException {
		String parts[] = idPassword.split(":", 2);
		byte digest[] = MessageDigest.getInstance("SHA1").digest(idPassword.getBytes());
		return parts[0] + ":" + Base64Util.encode(digest);
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "config.xml" });
		ZkClient zkClient = (ZkClient) ctx.getBean("zkClient");
		System.out.println(zkClient.exists(ConfigConstant.CONFIG_INFO_PATH));

		// System.out.println(DigestAuthenticationProvider.generateDigest("admin:"));
		// admin:x1nq8J5GOJVPY6zgzhtTtA9izLc=
	}

}

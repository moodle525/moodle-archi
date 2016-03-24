package com.doer.moodle.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

import com.doer.moodle.common.contants.ConfigConstant;

public class CuratorAuthTest {
	CuratorFramework client;

	@Before
	public void before() {
		client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.authorization("digest", "admin:admin".getBytes())
				.build();
		client.start();
	}

	@Test
	public void create() throws Exception {
		List<ACL> aclList = new ArrayList<ACL>();
		ACL acl = null;
		acl = new ACL(Perms.READ, new Id(ConfigConstant.AuthType.DIGEST, DigestAuthenticationProvider.generateDigest("admin:admin")));
		aclList.add(acl);
		client.create().creatingParentsIfNeeded().withACL(aclList).forPath(path);
	}

	@Test
	public void delete() throws Exception {
		// List<ACL> aclList = new ArrayList<ACL>();
		// ACL acl = null;
		// acl = new ACL(Perms.ALL, new Id(ConfigConstant.Scheme.DIGEST,
		// "admin:admin"));
		// aclList.add(acl);
		client.delete().forPath(path);
	}

	@Test
	public void get() throws Exception {
		byte[] forPath = client.getData().forPath(path);
		System.out.println(new String(forPath));
	}

	String path = "/laa/zh";
}

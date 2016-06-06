package com.doer.moodle.test.distribute.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorDemo {
	public static void main(String[] args) throws Exception {

		// 操作失败重试机制 1000毫秒间隔 重试3次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		// 创建Curator客户端
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		// 开始
		client.start();

		/**
		 * 这个类是线程安全的，一个JVM创建一个就好 mylock 为锁的根目录，我们可以针对不同的业务创建不同的根目录
		 */
		final InterProcessMutex lock = new InterProcessMutex(client, "/mylock");
		try {
			// 阻塞方法，获取不到锁线程会挂起。
			lock.acquire();
			System.out.println("已经获取到锁");
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放锁，必须要放到finally里面，已确保上面方法出现异常时也能够释放锁。
			lock.release();
		}

		Thread.sleep(10000);

		client.close();
	}
}

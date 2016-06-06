package com.doer.moodle.test.distribute.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

/*
 读写锁：

本文在前面俩片的基础之上介绍如何 使用ZooKeeper实现Java跨JVM的分布式锁(读写锁)。


简单介绍一下读写锁，在使用读写锁时， 多个客户端（线程）可以同时获取 “读锁”， 但是“写入锁”是排它的，只能单独获取。

1、假设A,B线程获取到 “读锁”， 这时C线程就不能获取 “写锁”。

2、假设C线程获取了“写锁”，那么A,B线程就不能获取“读锁”。

这在某种情况下会大幅度提高系统的性能，在单JVM进程内 Java已经提供了这种锁的机制，可以参考ReentrantReadWriteLock这个类。
 */
/**
 * 实现原理：
 * 
 * 实现原理与之前介绍的锁的原理基本类似，这里主要说明一下不同之处。
 * 
 * 1、写入锁在申请锁时写入的节点名称是这样的 xxxx-__WRIT__00000000xxx 例如：
 * _c_9b6e456b-94fe-47e7-b968-34027c094b7d-__WRIT__0000000006
 * 
 * 2、读取锁在申请锁时写入的节点名称是这样的 xxxx-__READ__00000000xxx 例如：
 * _c_9b6e456b90-9c33-6294665cf525--b6448-__READ__0000000005
 * 
 * 区别就是写入锁的字符串包含WRIT，读取所包含READ
 * 
 * 
 * 获取锁的区别：
 * 
 * 1、写入锁在获取锁时的处理与前面文章介绍的原理一直，就是判断自己前面还有没有节点，如果没有就可以获取到锁，如果有就等待前面的节点释放锁。
 * 
 * 2、读取锁在获取锁时的处理是，判断自己前面还有没有写入锁的节点，也就是前面的节点是否包含WRIT，如果有那么等待前面的节点释放锁。
 * 
 * 读取所自己前面有 其它 读取锁节点 无所谓，它仍然可以获取到锁，这也就是读取所可以多客户端共享的原因。
 * 
 */
public class ReadWriteLock {
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		client.start();

		InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, "/read-write-lock");

		// 读锁
		final InterProcessMutex readLock = readWriteLock.readLock();
		// 写锁
		final InterProcessMutex writeLock = readWriteLock.writeLock();

		try {
			readLock.acquire();
			System.out.println(Thread.currentThread() + "获取到读锁");

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 在读锁没释放之前不能读取写锁。
						writeLock.acquire();
						System.out.println(Thread.currentThread() + "获取到写锁");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							writeLock.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			// 停顿3000毫秒不释放锁，这时其它线程可以获取读锁，却不能获取写锁。
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readLock.release();
		}

		Thread.sleep(1000000);
		client.close();
	}
}

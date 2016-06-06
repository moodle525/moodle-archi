package com.doer.moodle.test.distribute.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 如果说我们有A,B 二台服务器进行集群，这10个线程获取锁的请求分别来自这2台服务器，例如（1-5）号线程来自A服务器，
 * （6-10）号线程来自B服务器，那么它们与zk交互了10次，创建10个znode来申请锁，但是如果我们进行一定的优化，它们只要与zk交互2次就够了，
 * 我们来把上面的逻辑改造一下。
 * 
 * 1、 首选根据key获取资源，如果资源存在，用之。
 * 
 * 2、如果不存在，则申请获取锁（JVM进程内的锁)。
 * 
 * 3、获取到锁(JVM进程内的锁)，再次判断资源是否存在，如果资源存在就退出，没啥好所的。
 * 
 * 4、如果资源不存在，则申请获取锁(分布式锁)。
 * 
 * 5、获取到锁（分布式锁）再次判断资源是否存在（防止重复更新），如果存在说明已经有人更新了，方法退出，否则更新缓存。
 * 
 * 6、释放分布式锁，释放JVM进程内的锁。
 */
public class Main {

	// 我们用一个static的map模拟一个第三方独立缓存
	public static Map<String, Object> redis = new HashMap<String, Object>();
	public static final String key = "redisKey";

	public static void main(String[] args) throws InterruptedException {
		// 创建俩个对象分别模拟2个进程
		RedisProcess processA = new RedisProcess();
		RedisProcess processB = new RedisProcess();

		// 每个进程别分用50个线程并发请求
		ExecutorService service = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 50; i++) {
			service.execute(processA);
			service.execute(processB);
		}

		service.shutdown();
		service.awaitTermination(30, TimeUnit.SECONDS);
	}

	public static class RedisProcess implements Runnable {
		CuratorFramework client;
		// ZK分布式锁
		InterProcessMutex distributedLock;
		// JVM内部锁
		ReentrantLock jvmLock;

		public RedisProcess() {
			client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
			client.start();
			distributedLock = new InterProcessMutex(client, "/mylock", new NoFairLockDriver());
			jvmLock = new ReentrantLock();
		}

		@Override
		public void run() {
			// (1)首先判断缓存内资源是否存在
			if (redis.get(key) == null) {
				try {

					// 这里延时1000毫秒的目的是防止线程过快的更新资源，那么其它线程在步骤(1)处就返回true了.
					Thread.sleep(1000);

					// 获取JVM锁(同一进程内有效)
					jvmLock.lock();

					// (2)再次判断资源是否已经存在
					if (redis.get(key) == null) {
						System.out.println("线程:" + Thread.currentThread() + "获取到JVM锁，redis.get(key)为空, 准备获取ZK锁");

						// 这里延时500毫秒的目的是防止线程过快更新资源，其它线程在步骤(2)就返回true了。
						Thread.sleep(500);
						try {
							// 获取zk分布式锁
							distributedLock.acquire();
							System.out.println("线程:" + Thread.currentThread() + "获取到JVM锁，redis.get(key)为空, 获取到了ZK锁");

							// 再次判断,如果为空这时可以更新资源
							if (redis.get(key) == null) {
								redis.put(key, Thread.currentThread() + "更新了缓存......");
								System.out.println("线程:" + Thread.currentThread() + "更新了缓存");
							} else {
								System.out.println("线程:" + Thread.currentThread() + "当前资源已经存在，不需要更新");
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							// 释放ZK锁
							try {
								distributedLock.release();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println(
								"线程:" + Thread.currentThread() + "获取到JVM锁，redis.get(key)不为空," + redis.get(key));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 释放JVM锁
					jvmLock.unlock();
				}
			} else {
				System.out.println(redis.get(key));
			}
		}
	}

}
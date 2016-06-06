package com.doer.moodle.test.distribute.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.StandardLockInternalsDriver;
import org.apache.zookeeper.CreateMode;

/**
 * 锁原理： 1、首先要创建一个锁的根节点，比如/mylock。
 * 
 * 2、想要获取锁的客户端在锁的根节点下面创建znode，作为/mylock的子节点，节点的类型要选择CreateMode.
 * PERSISTENT_SEQUENTIAL，节点的名字最好用uuid（至于为什么用uuid我后面会讲，先说一下~如果不这么做在某种情况下会发生死锁，
 * 这一点我看了很多国内朋友自己的实现，都没有考虑到这一层，这也是我为什么不建议大家自己去封装这种锁，因为它确实很复杂），假设目前同时有3个客户端想要获得锁，
 * 那么/mylock下的目录应该是这个样子的。
 * xxx-lock-0000000001，xxx-lock-0000000002，xxx-lock-0000000003 xxx为uuid ，
 * 0000000001，0000000002，0000000003 是zook服务端自动生成的自增数字。
 * 
 * 3、当前客户端通过getChildren（/mylock）获取所有子节点列表并根据自增数字排序，然后判断一下自己创建的节点的顺序是不是在列表当中最小的，
 * 如果是 那么获取到锁，如果不是，那么获取自己的前一个节点，并设置监听这个节点的变化，当节点变化时重新执行步骤3 直到自己是编号最小的一个为止。
 * 举例：假设当前客户端创建的节点是0000000002，因为它的编号不是最小的，所以获取不到锁，那么它就找到它前面的一个节点0000000001
 * 并对它设置监听。
 * 
 * 4、释放锁，当前获得锁的客户端在操作完成后删除自己创建的节点，这样会激发zook的事件给其它客户端知道，这样其它客户端会重新执行（步骤3）。
 * 举例：加入客户端0000000001获取到锁，然后客户端0000000002加入进来获取锁，发现自己不是编号最小的，那么它会监听它前面节点的事件（
 * 0000000001的事件）然后执行步骤（3），当客户端0000000001操作完成后删除自己的节点，这时zook服务端会发送事件，
 * 这时客户端0000000002会接收到该事件，然后重复步骤3直到获取到锁）
 * 
 * 上面的步骤实现了一个有序锁，也就是先进入等待锁的客户端在锁可用时先获得锁。
 * 如果想要实现一个随机锁，那么只需要把PERSISTENT_SEQUENTIAL换成一个随机数即可。
 */
public class NoFairLockDriver extends StandardLockInternalsDriver {

	/**
	 * 随机数的长度
	 */
	private int numLength;
	private static int DEFAULT_LENGTH = 5;

	public NoFairLockDriver() {
		this(DEFAULT_LENGTH);
	}

	public NoFairLockDriver(int numLength) {
		this.numLength = numLength;
	}

	@Override
	public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
		String newPath = path + getRandomSuffix();
		String ourPath;
		if (lockNodeBytes != null) {
			// 原来使用的是CreateMode.EPHEMERAL_SEQUENTIAL类型的节点
			// 节点名称最终是这样的_c_c8e86826-d3dd-46cc-8432-d91aed763c2e-lock-0000000025
			// 其中0000000025是zook服务器端资自动生成的自增序列 从0000000000开始
			// 所以每个客户端创建节点的顺序都是按照0，1，2，3这样递增的顺序排列的，所以他们获取锁的顺序与他们进入的顺序是一致的，这也就是所谓的公平锁
			// 现在我们将有序的编号换成随机的数字，这样在获取锁的时候变成非公平锁了
			ourPath = client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL)
					.forPath(newPath, lockNodeBytes);
			// ourPath =
			// client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path,
			// lockNodeBytes);
		} else {
			ourPath = client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL)
					.forPath(newPath);
			// ourPath =
			// client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
		}
		return ourPath;
	}

	/**
	 * 获得随机数字符串
	 */
	public String getRandomSuffix() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numLength; i++) {
			sb.append((int) (Math.random() * 10));
		}
		return sb.toString();
	}

}

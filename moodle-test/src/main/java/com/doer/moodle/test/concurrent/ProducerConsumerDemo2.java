package com.doer.moodle.test.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程中唤醒，即唤醒对方，也换新本方。
 * @author lixiongcheng
 *
 */
class Resource2 {
	private String name;
	private int count;
	private boolean flag;

	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

	public void set(String name) {
		lock.lock();
		try {
			while (flag) {
				condition.await();

			}
			this.name = name + count++;
			System.out.println(Thread.currentThread().getName() + "....生产者..."
					+ this.name);
			this.flag = true;
			condition.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void out() {
		lock.lock();
		try {
			while (!flag) {
				condition.await();
			}
			System.out.println(Thread.currentThread().getName() + "........消费者........."
					+ this.name);
			flag = false;
			condition.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}

class Producer2 implements Runnable {
	private Resource2 res;

	public Producer2(Resource2 res) {
		this.res = res;
	}

	@Override
	public void run() {
		while (true)
			res.set("zhangsan");
	}
}

class Consumer2 implements Runnable {
	private Resource2 res;

	public Consumer2(Resource2 res) {
		this.res = res;
	}

	@Override
	public void run() {
		while (true)
			res.out();
	}
}

public class ProducerConsumerDemo2 {
	public static void main(String[] args) {
		Resource2 res = new Resource2();
		new Thread(new Producer2(res)).start();
		new Thread(new Producer2(res)).start();
		new Thread(new Consumer2(res)).start();
		new Thread(new Consumer2(res)).start();
	}
}

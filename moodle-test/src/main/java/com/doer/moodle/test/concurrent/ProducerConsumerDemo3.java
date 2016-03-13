package com.doer.moodle.test.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 只唤醒对方
 * @author lixiongcheng
 *
 */
class Resource3 {
	private String name;
	private int count;
	private boolean flag;

	Lock lock = new ReentrantLock();
	Condition condition_produ = lock.newCondition();
	Condition condition_consu = lock.newCondition();

	public void set(String name) {
		lock.lock();
		try {
			while (flag) {
				condition_produ.await();

			}
			this.name = name + count++;
			System.out.println(Thread.currentThread().getName() + "....生产者..."
					+ this.name);
			this.flag = true;
			condition_consu.signal();
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
				condition_consu.await();
			}
			System.out.println(Thread.currentThread().getName() + "........消费者........."
					+ this.name);
			flag = false;
			condition_produ.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}

class Producer3 implements Runnable {
	private Resource3 res;

	public Producer3(Resource3 res) {
		this.res = res;
	}

	@Override
	public void run() {
		while (true)
			res.set("zhangsan");
	}
}

class Consumer3 implements Runnable {
	private Resource3 res;

	public Consumer3(Resource3 res) {
		this.res = res;
	}

	@Override
	public void run() {
		while (true)
			res.out();
	}
}

public class ProducerConsumerDemo3 {
	public static void main(String[] args) {
		Resource3 res = new Resource3();
		new Thread(new Producer3(res)).start();
		new Thread(new Producer3(res)).start();
		new Thread(new Consumer3(res)).start();
		new Thread(new Consumer3(res)).start();
	}
}

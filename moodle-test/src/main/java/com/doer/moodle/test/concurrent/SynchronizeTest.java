package com.doer.moodle.test.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class SynchronizeTest implements Runnable {
	static int count = 0;
	ReentrantLock lock = new ReentrantLock();

	@Override
	public void run() {
		for (int i = 0; i < 100000; i++) {
			lock.lock();
			try {
				count++;
			} finally {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		SynchronizeTest s = new SynchronizeTest();
		Thread t1 = new Thread(s);
		Thread t2 = new Thread(s);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(count);

	}

}

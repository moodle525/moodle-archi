package com.doer.moodle.test.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(10);
		FireDemo f = new FireDemo(latch);
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			pool.execute(f);
		}
		f.await();
		System.out.println("Fire...");
	}

}

class FireDemo implements Runnable {
	private CountDownLatch count;

	public FireDemo(CountDownLatch count) {
		this.count = count;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(new Random().nextInt(10) * 1000);
			System.out.println(Thread.currentThread() + " complete!");
			count.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void await() throws InterruptedException{
		count.await();
	}
}

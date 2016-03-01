package com.doer.moodle.test.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TransferQueueDemo {
	public static void main(String[] args) throws InterruptedException {
		testTansferQueue();
	}

	public static void testTansferQueue() throws InterruptedException {
		final TransferQueue<String> transferQueue = new LinkedTransferQueue<String>();

		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable() {
			public void run() {
				try {
					// 此处阻塞，等待take()，poll()的发生。
					transferQueue.transfer("test");
					System.out.println("子线程完成传递.");
				} catch (InterruptedException e) {
				}
			}
		});

		// 此处阻塞，等待trankser(当然可以是别的插入元素的方法)的发生
		String test = transferQueue.take();
		System.out.printf("主线程完成获取 %s.\n", test);
		Thread.sleep(1000);

	}
}

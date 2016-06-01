package com.doer.moodle.test.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableFutureTest {
	public static void main(String[] args) throws Exception, Exception{
		ExecutorService threadPools = Executors.newCachedThreadPool();
		Future<String> res = threadPools.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(3000);
				return "hello";
			}
		});
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("wait...");
		System.out.println(res.get());
	}
}

package com.doer.moodle.test;

public class VolatileTest extends Thread {
	private volatile boolean stop = false;

	public void stopMe() {
		stop = true;
	}

	public void run() {
		int i = 0;
		while (!stop) {
			i++;
			System.out.println(i);
		}
		System.out.println("Stop Thread!");
	}
	
	public static void main(String[] args) throws Exception{
		VolatileTest t =new  VolatileTest();
		t.start();
		Thread.sleep(1000);
		t.stopMe();
		Thread.sleep(1000);
	}
}

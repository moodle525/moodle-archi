package com.doer.moodle.test.concurrent;

import java.util.LinkedList;
import java.util.List;

public class NotifyTest {

	public static void main(String[] args) throws InterruptedException {
		List<String> list = new LinkedList<String>();
		Thread t1 = new Thread(new Task(list));
		t1.start();
		
		Thread.sleep(5000);
		System.out.println("**********");
		Cus cus = new Cus(list);
		cus.go();
	}

}

class Cus {
	private List<String> list;

	public Cus(List<String> list) {
		this.list = list;
	}

	public void go() {
		synchronized (list) {
			list.add("as");
			list.notifyAll();
		}
	}
}

class Task implements Runnable {
	private List<String> list;

	public Task(List<String> list) {
		this.list = list;
	}

	public void run() {
		synchronized (list) {
			while (list.size() == 0) {
				try {
					list.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("sssssssssssssssssssssss");
		}
	}
}

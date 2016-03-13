package com.doer.moodle.test.concurrent;

/**
 * 只有两个线程，用if判断，当线程数量多于2时，需要用while循环判断，用notifyAll唤醒，缺点：不仅唤醒对方，还唤醒本方，争夺资源，
 * 导致执行效率降低
 * 
 * @author lixiongcheng
 *
 */
class Resource {
	private String name;
	private int count;
	private boolean flag;

	public synchronized void set(String name) throws InterruptedException {
		if (flag) {
			this.wait();
		}
		this.name = name + count++;
		System.out.println(Thread.currentThread().getName() + "....生产者..."
				+ this.name);
		this.flag = true;
		this.notify();
	}

	public synchronized void out() throws InterruptedException {
		if (!flag) {
			this.wait();
		}
		System.out.println(Thread.currentThread().getName() + "....消费者..."
				+ this.name);
		flag = false;
		this.notify();
	}
}

class Producer implements Runnable {
	private Resource res;

	public Producer(Resource res) {
		this.res = res;
	}

	@Override
	public void run() {
		try {
			while (true)
				res.set("zhangsan");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {
	private Resource res;

	public Consumer(Resource res) {
		this.res = res;
	}

	@Override
	public void run() {
		try {
			while (true)
				res.out();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public class ProducerConsumerDemo {
	public static void main(String[] args) {
		Resource res = new Resource();
		new Thread(new Producer(res)).start();
		new Thread(new Consumer(res)).start();
	}
}

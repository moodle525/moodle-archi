package com.doer.moodle.test.concurrent;

public class TicketDemo {
	public static void main(String[] args) {
		Ticket t1 = new Ticket();
		new Thread(t1).start();
		new Thread(t1).start();
		new Thread(t1).start();
		new Thread(t1).start();
	}
}

class Ticket implements Runnable {
	private int tick = 100;

	@Override
	public void run() {
		while (true) {
			synchronized (this) {
				try {
					if (this.tick > 0) {
						Thread.sleep(10);
						System.out.println(Thread.currentThread() + "..." + tick--);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}

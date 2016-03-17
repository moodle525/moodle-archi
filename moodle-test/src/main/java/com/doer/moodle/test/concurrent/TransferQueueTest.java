package com.doer.moodle.test.concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 开启码农生活，发现JDK7新出的一个非常屌的东西TransferQueue，没想到还能发说说，😄😄😄😄
 * TransferQueue学习
 * @author lixiongcheng
 *
 */
public class TransferQueueTest {
	public static void main(String[] args) {
		TransferQueue<Goods> goodsQueue = new LinkedTransferQueue<Goods>();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(new GoodsProducer(goodsQueue));
		for (int i = 0; i < 100000; i++) {
		}

		GoodsConsumer gc = new GoodsConsumer(goodsQueue);
		executorService.execute(gc);
	}
}

class Goods {
	private String name;
	private String color;

	public Goods(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}

class GoodsConsumer implements Runnable {
	private TransferQueue<Goods> goodsQueue = null;

	public GoodsConsumer(TransferQueue<Goods> goodsQueue) {
		this.goodsQueue = goodsQueue;
	}

	public void run() {
		while (true) {
			Goods goods = null;
			try {
				goods = goodsQueue.poll(1000, TimeUnit.MILLISECONDS);
				Thread.sleep(500);
				System.out.println(".." + Thread.currentThread().getName()
						+ "..consumer...size:(" + goodsQueue.size() + ").."
						+ goods.getName() + ".." + goods.getColor());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class GoodsProducer implements Runnable {
	private TransferQueue<Goods> goodsQueue = null;

	private final int queueSize = 10;

	public GoodsProducer(TransferQueue<Goods> goodsQueue) {
		this.goodsQueue = goodsQueue;
	}

	public void run() {
		while (true) {
			Goods goods = createGoods();
			if (!goodsQueue.tryTransfer(goods)) {
				try {
					if (goodsQueue.size() < queueSize) {
						Thread.sleep(100);
						goodsQueue.put(goods);
					} else {
						goodsQueue.transfer(goods);
					}
					System.out
							.println("...." + Thread.currentThread().getName()
									+ ".......produce...size:("
									+ goodsQueue.size() + ").."
									+ goods.getName() + ".." + goods.getColor());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Goods createGoods() {
		return new Goods("good" + new Random().nextInt(10), "color"
				+ new Random().nextInt(10));
	}
}

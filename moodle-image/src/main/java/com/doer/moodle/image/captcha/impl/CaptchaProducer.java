package com.doer.moodle.image.captcha.impl;

import java.util.concurrent.TransferQueue;

import com.doer.moodle.image.captcha.Captcha;

public class CaptchaProducer implements Runnable {
	private TransferQueue<Captcha> captchaQueue = null;
	private boolean stop = false;

	private int width;
	private int height;
	private int charCount;
	private int queueSize = 10;

	public CaptchaProducer(TransferQueue<Captcha> captchaQueue, int width,
			int height, int charCount) {
		this.captchaQueue = captchaQueue;
		this.width = width;
		this.height = height;
		this.charCount = charCount;
	}

	@Override
	public void run() {
		Captcha captcha = null;
		while (!stop) {
			// 生成图片
			captcha = CaptchaFactory.genImage(width, height, charCount);
			// 增加到队列
			//transfer：如果消费端在等待获取数据，则推给消费端，并返回true，否则false
			if (!captchaQueue.tryTransfer(captcha)) {//无消费端，则把验证码对象放入队列中
				try {
					if (captchaQueue.size() < queueSize) {
						captchaQueue.put(captcha);
					}else {
						captchaQueue.transfer(captcha);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}

package com.doer.moodle.image.captcha.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import com.doer.moodle.image.captcha.Captcha;

public class CaptchaConsumer {
	private TransferQueue<Captcha> captchaQueue = null;
	private int waitTime = 100;

	private int width;
	private int height;
	private int charCount;

	public CaptchaConsumer(TransferQueue<Captcha> captchaQueue, int waitTime,
			int width, int height, int charCount) {
		this.captchaQueue = captchaQueue;
		this.width = width;
		this.height = height;
		this.charCount = charCount;
	}

	public Captcha getCaptcha() {
		Captcha captcha = null;
		try {
			//Retrieves and removes the head of this queue, waiting 
			//up to the specified wait time if necessary for an element to become available.
			captcha = captchaQueue.poll(waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (null == captcha) {
			captcha = CaptchaFactory.genImage(width, height, charCount);
		}
		return captcha;
	}
}

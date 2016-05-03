package com.doer.moodle.test.event;

public class DemoEventListenerImpl1 implements DemoEventListener{

	@Override
	public void doDemo(DemoEventObject eventObj) {
		System.out.println("监听器1完成其任务");
	}

}

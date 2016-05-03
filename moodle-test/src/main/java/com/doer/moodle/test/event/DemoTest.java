package com.doer.moodle.test.event;

public class DemoTest {
	public static void main(String[] args) {
		DemoEventSource des = new DemoEventSource();
		des.addListener(new DemoEventListenerImpl1());
		des.notifyDemoEvent();
	}
}

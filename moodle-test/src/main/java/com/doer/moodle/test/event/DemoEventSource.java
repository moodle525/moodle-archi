package com.doer.moodle.test.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件源，监听器管理
 * 
 * @author e00769a
 *
 */
public class DemoEventSource {
	private List<DemoEventListener> listeners = new ArrayList<DemoEventListener>();

	public void addListener(DemoEventListener l) {
		if (l != null)
			listeners.add(l);
	}

	public void removeListener(DemoEventListener l) {
		if (l != null)
			listeners.remove(l);
	}
	
	public void notifyDemoEvent(){
		for(DemoEventListener l : listeners){
			l.doDemo(new DemoEventObject(this));
		}
	}
}

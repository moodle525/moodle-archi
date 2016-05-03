package com.doer.moodle.test.event;

import java.util.EventListener;

/**
 * 时间监听器，需要事件对象来协作完成监听任务
 * @author e00769a
 *
 */
public interface DemoEventListener extends EventListener{
	public abstract void doDemo(DemoEventObject eventObj);
	
}

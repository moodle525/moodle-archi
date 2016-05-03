package com.doer.moodle.test.event;

import java.util.EventObject;

/**
 * 事件对象 extends java.util.EventObject</br>
 * 事件对象：如点击、注册、登录都可叫做事件对象
 * @author e00769a
 *
 */
@SuppressWarnings("serial")
public class DemoEventObject extends EventObject{
	/**
	 * 事件源，比如按钮、鼠标
	 */
	private Object source;

	public DemoEventObject(Object source) {
		super(source);
		this.source = source;
	}

}

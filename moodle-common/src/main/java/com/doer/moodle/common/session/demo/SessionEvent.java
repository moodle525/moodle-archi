package com.doer.moodle.common.session.demo;

public interface SessionEvent {
	public static final String ATTRIBUTE_CHANGED = SessionEventEnum.ATTRIBUTE_CHANGED.toString();
	public static final String INVALIDATED = SessionEventEnum.INVALIDATED.toString();

	public abstract SessionEventEnum getEventTye();
}

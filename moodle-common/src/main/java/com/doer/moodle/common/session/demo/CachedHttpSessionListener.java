package com.doer.moodle.common.session.demo;

import javax.management.RuntimeErrorException;

public class CachedHttpSessionListener implements SessionListener{

	@Override
	public void handle(SessionEvent event) {
		switch (event.getEventTye()) {
		case ATTRIBUTE_CHANGED:
			break;
		case INVALIDATED:
			
			break;
		default:
			throw new RuntimeErrorException(null, "no event");
		}
	}

}

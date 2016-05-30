package com.doer.moodle.common.sess.session;

import com.doer.moodle.common.sess.session.impl.CacheHttpSession;

public abstract interface SessionListener {
	  public abstract void onAttributeChanged(CacheHttpSession paramRedisHttpSession);

	  public abstract void onInvalidated(CacheHttpSession paramRedisHttpSession);
}

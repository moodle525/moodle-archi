package com.doer.moodle.common.session;

import javax.servlet.ServletContext;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class CachedHttpSessionApplicationInitializer extends AbstractHttpSessionApplicationInitializer {

	public CachedHttpSessionApplicationInitializer() {
		super(CachedHttpSessionConfig.class);
	}
	
	@Override
	protected void beforeSessionRepositoryFilter(ServletContext servletContext) {

	}
	
	@Override
	protected void afterSessionRepositoryFilter(ServletContext servletContext) {

	}

}

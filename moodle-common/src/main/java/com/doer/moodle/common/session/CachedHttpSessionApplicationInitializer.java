package com.doer.moodle.common.session;

import javax.servlet.ServletContext;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * 应用服务器启动，执行父类的onStartup方法
 * @author lixiongcheng 
 *
 */
public class CachedHttpSessionApplicationInitializer extends AbstractHttpSessionApplicationInitializer {

	/**
	 * 容器启动newInstance，执行无参数构造函数
	 */
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

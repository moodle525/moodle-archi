package com.doer.moodle.common.sess.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.paas.PaasConstant;
import com.ai.paas.PaasContextHolder;
import com.ai.paas.session.ILocaleCodeHandlerSV;
import com.ai.paas.session.impl.RequestEventSubject;
import com.ai.paas.util.LocaleCodeUtil;

public class LocaleFilter implements Filter {
	
	//日志；
	private static Logger logger = LoggerFactory.getLogger(LocaleFilter.class);
	
	private String localeCodeHandlerClazz;
	
	//基于localCodeHandlerSV
	private ILocaleCodeHandlerSV localeCodeHandlerSV;

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		HttpSession session = request.getSession(false);
		
		/**
		 * 获取 session的相关信息，如果session的信息非空，而且 注入的loacalCodeHandlerSV非空，那么采用诸如的 SV 对LocaleCodeUtil进行处理；
		 * 否则，按照之前的方法，通过session的值处理；
		 */
		if (null != session){
			if(this.localeCodeHandlerSV == null){
				if(null != session.getAttribute(PaasConstant.SESSION_KEY_LOCALE_CODE)) {
				    LocaleCodeUtil.setLocaleCode((String) session.getAttribute(PaasConstant.SESSION_KEY_LOCALE_CODE));
			    }
			} else {
				this.localeCodeHandlerSV.doHandler(session);
			}
		}
				
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		RequestEventSubject eventSubject = new RequestEventSubject();
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			eventSubject.completed(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		String localeCodeHandler = arg0.getInitParameter("localeCodeHandler");
		if(StringUtils.isNotBlank(localeCodeHandler)){
			this.localeCodeHandlerClazz = localeCodeHandler;
			Object obj = PaasContextHolder.getBean(localeCodeHandler);
			if(obj == null){
				this.localeCodeHandlerSV = null;
			} else {
				this.localeCodeHandlerSV = (ILocaleCodeHandlerSV)obj;
			}
			logger.info("初始化，localeCodeHandlerClazz 为：" + this.localeCodeHandlerClazz);
		}
		
	}

}

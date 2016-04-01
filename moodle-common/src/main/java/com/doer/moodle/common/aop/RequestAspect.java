package com.doer.moodle.common.aop;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doer.moodle.common.contants.Constant;
import com.google.gson.Gson;

/**
 * 请求信息切面
 * 
 * @author lixiongcheng
 *
 */
@Component
@Aspect
public class RequestAspect {
	private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private HttpServletRequest request;

	@Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void doRequest() {
	}

	@Before(value = "doRequest()")
	public void beforeRequest() {
		try {
			request.setCharacterEncoding(Constant.CHARSET);
			String uri = request.getRequestURI();
			String remoteAddr = request.getRemoteAddr();
			String params = getParamsOfJson();
			log.info("requst...uri:" + uri + ",params:" + params + ",from " + remoteAddr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getParamsOfJson() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> paramNames = request.getParameterNames();
		for (Enumeration<String> e = paramNames; e.hasMoreElements();) {
			String paraName = e.nextElement();
			String paraValue = request.getParameter(paraName);
			map.put(paraName, paraValue == null ? "" : new String(paraValue.getBytes("ISO-8859-1"), "UTF-8"));
		}
		return new Gson().toJson(map);
	}
}

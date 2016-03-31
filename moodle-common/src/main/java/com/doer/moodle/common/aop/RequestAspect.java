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

import com.google.gson.Gson;

@Component
@Aspect
public class RequestAspect {
	private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
	
	@Autowired
	private HttpServletRequest request;

	@Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void doRequest() {
	}
	
	@Before(value="doRequest()")
	public void beforeRequest(){
		String contextPath = request.getContextPath();
		String remoteAddr = request.getRemoteAddr();
		String params = getParamsOfJson();
		log.info("requst...url:"+contextPath+",params:"+params+",from "+remoteAddr);
	}
	
	private String getParamsOfJson(){
		Map<String,String> map = new HashMap<String,String>();
		Enumeration<String> paraNames = request.getAttributeNames();
		for(Enumeration<String> e=paraNames;e.hasMoreElements();){
		       String paraName=e.nextElement();
		       String paraValue=request.getParameter(paraName);
		       map.put(paraName, paraValue==null?"":paraValue);
		}
		return new Gson().toJson(map);
	}
}

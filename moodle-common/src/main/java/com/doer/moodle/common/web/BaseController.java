package com.doer.moodle.common.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.doer.moodle.common.contants.SessionConstant;


public abstract class BaseController {
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	protected HttpSession session;
	
	@Autowired
	protected ServletContext servletContext;
	
	public String getParameter(String key){
		return request.getParameter(key);
	}
	
	public void setAttribute(String key,Object obj){
		request.setAttribute(key, obj);
	}
	
	public Object getLoginUser(){
		return session.getAttribute(SessionConstant.USER_SESSION_KEY);
	}
}

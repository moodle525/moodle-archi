package com.doer.moodle.web.controller;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doer.moodle.common.annotation.SystemLog;
import com.doer.moodle.common.web.R;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;

@Controller
@RequestMapping(value="/")
public class IndexController {
	
	@Value(value="${jdbc.url}")
	private String v;
	
	@ResponseBody
	@RequestMapping(value="/index")
	@SystemLog
	public R<PmsUserInfo> index(HttpServletRequest request){
		PmsUserInfo pui = new PmsUserInfo();
		pui.setUserName("张三");
		pui.setRemark(v);
		pui.setCreateTime(new Date(System.currentTimeMillis()));
		pui.setUserNo("123");
		System.out.println("controller back");
		
		HttpSession session = request.getSession();
		session.setAttribute("z", "sssss");
		return new R<PmsUserInfo>(pui);
	}
	
	@ResponseBody
	@RequestMapping(value="/get")
	public R<String> get(HttpServletRequest request){
		HttpSession session = request.getSession();
		String value = (String)session.getAttribute("z");
		String id = session.getId();
		return new R<String>(value+"..."+id);
	}
}

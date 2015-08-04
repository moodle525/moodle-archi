package com.doer.moodle.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.doer.moodle.common.entity.R;
import com.doer.moodle.interfaces.IUser;
import com.doer.moodle.interfaces.entity.UserInfo;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Reference
	private IUser user;
	
	@RequestMapping(value = "/toUserList")
	public String toUserList(HttpServletRequest request) {
		List<UserInfo> userList = user.getUsers();
		request.setAttribute("userList", userList);
		return "/user/userList";
	}

	@RequestMapping(value = "/addUser")
	public String addUser(HttpServletRequest request,
			@RequestParam String userName, @RequestParam String password) {
		user.saveUser(userName, password);
		return toUserList(request);
	}
	
	@RequestMapping(value="/search")
	@ResponseBody
	public R<List<UserInfo>> search(@RequestParam String key){
		List<UserInfo> data = user.serach(key);
		return new R<List<UserInfo>>(data);
	}

	@RequestMapping(value = "/getUsers")
	@ResponseBody
	public R<List<UserInfo>> getUsers() {
		List<UserInfo> data = user.getUsers();
		return new R<List<UserInfo>>(data);
	}
	

}

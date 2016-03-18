package com.doer.moodle.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.doer.moodle.dubbo.interfaces.IPmsUserService;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/beans.xml","/dubbo/consumer.xml" })
@Transactional(rollbackFor = Exception.class)
public class PmsUserTest {
	@Autowired
	private IPmsUserService iPmsUserService;
	
	private Gson gson = new Gson();
	
	@Test
	public void getUser(){
		PmsUserInfo pmsUserInfo = iPmsUserService.getById(1L);
		System.out.println(gson.toJson(pmsUserInfo));
	}
}

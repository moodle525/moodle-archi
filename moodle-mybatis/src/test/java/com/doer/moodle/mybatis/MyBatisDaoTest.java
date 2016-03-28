package com.doer.moodle.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;
import com.doer.moodle.mybatis.entity.PmsUser;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/beans.xml" })
@TransactionConfiguration
public class MyBatisDaoTest {
	private Gson gson = new Gson();

	@Autowired
	IPmsUserDao pmsUserdao;

	@Test
	public void getById() {
		PmsUser user = pmsUserdao.getById(1);
		System.out.println(gson.toJson(user));
	}

	@Test
	public void save() throws Exception {
		PmsUser user = new PmsUser();
		user.setUserName("张三");
		user.setUserNo("11ass11");
		user.setUserPwd("1111111111111111111111111111");
		user.setEmail("455@qq.com");
		user.setRemark("4444");
		user.setStatus(0);
		user.setUserType("D");
		pmsUserdao.insert(user);
	}

	@Test
	public void find() {
		PmsUser user = pmsUserdao.findByUserNo("111");
		System.out.println(gson.toJson(user));
	}

}

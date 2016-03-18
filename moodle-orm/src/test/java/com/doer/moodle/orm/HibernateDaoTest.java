package com.doer.moodle.orm;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.doer.moodle.orm.dao.intf.IUserDao;
import com.doer.moodle.orm.entity.User;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/beans.xml"})
@Transactional
public class HibernateDaoTest {
	Gson gson = new Gson();
	
	@Autowired
	IUserDao userDao;
	
	@Test
	public void test1(){
		List<User> users = userDao.getUsers();
		System.out.println(gson.toJson(users));
	}
	
}

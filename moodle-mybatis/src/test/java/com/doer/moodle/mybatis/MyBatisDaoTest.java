package com.doer.moodle.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;
import com.doer.moodle.mybatis.entity.PmsUser;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/beans.xml"})
@Transactional
public class MyBatisDaoTest {
	private Gson gson = new Gson();
	
	@Autowired
	IPmsUserDao pmsUserdao;
	
	@Test
	public void test1(){
		PmsUser user = pmsUserdao.getById(1);
		System.out.println(gson.toJson(user));
	}
}

package com.doer.moodle.dubbo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.doer.moodle.interfaces.IUser;
import com.doer.moodle.interfaces.entity.UserInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/beans.xml","/dubbo/consumer.xml" })
@Transactional(rollbackFor = Exception.class)
public class UserTest {

	@Reference
	private IUser user;
	
	
	@Test
	public void getUsers(){
		List<UserInfo> users = user.getUsers();
		System.out.println(users.get(0).getName());
	}
}

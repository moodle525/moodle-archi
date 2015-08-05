package com.doer.moodle.dubbo.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.doer.moodle.interfaces.IUser;
import com.doer.moodle.interfaces.entity.UserInfo;
import com.doer.moodle.orm.dao.intf.IUserDao;
import com.doer.moodle.orm.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class Usermpl implements IUser {
	private static final Logger log = Logger.getLogger(Usermpl.class);
	@Autowired
	private IUserDao userDao;

	@Override
	public List<UserInfo> getUsers() {
		log.info("-------");
		List<User> users = userDao.getUsers();
		List<UserInfo> infos = new ArrayList<UserInfo>();
		try {
			for (User user : users) {
				UserInfo userInfo = new UserInfo();
				PropertyUtils.copyProperties(userInfo, user);
				infos.add(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	@Override
	public void saveUser(String userName, String password) {
		User user = new User();
		user.setName(userName);
		user.setPassword("sss");
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
		user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		log.info("----" + userName + "---" + password);
		userDao.save(user);
	}
/**
 * hsaasaihoahdoiqhdoiqhwoidhqodwh
 * afewfqeoahico
 */
	@Override
	public List<UserInfo> serach(String key) {
		List<User> users = userDao.search(key);
		List<UserInfo> infos = new ArrayList<UserInfo>();
		try {
			for (User user : users) {
				UserInfo userInfo = new UserInfo();
				PropertyUtils.copyProperties(userInfo, user);
				infos.add(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	/**
	 * 啊代发费全额付全额付清费全额付清而非
	 *
	 * 啊代发费全额付全额付清费全额付清而非
	 *
	 * 啊代发费全额付全额付清费全额付清而非
	 */
	
	
}

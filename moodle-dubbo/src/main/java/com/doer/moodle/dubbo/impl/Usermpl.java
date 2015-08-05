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

/**
 * 演示一个如何提交代码。然后在我这就可以更新到。你以后想开发就可以直接提交。
 * 现在这些报错，是因为我这nexus没有公网出去，以后有空，我打算把它放到阿里云上，基础设备都公网出去，然后开源。
 * 现在你可以在你本地部署一个nexus，http://127.0.0.1:8081/nexus/content/repositories/central就可以了
 * 开始做提交。
 * 还有 moodle是父工程，moodle-xxx是其下模块。
 * @Description 
 * @author Administrator
 * @date 2015年8月5日 下午11:16:27 
 * @version V1.3.1
 */
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

}

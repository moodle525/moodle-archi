package com.doer.moodle.orm.dao.intf;

import java.util.List;

import com.doer.moodle.orm.dao.IBaseDao;
import com.doer.moodle.orm.entity.User;

public interface IUserDao extends IBaseDao<User>{

	List<User> getUsers();

	List<User> search(String key);
	
}

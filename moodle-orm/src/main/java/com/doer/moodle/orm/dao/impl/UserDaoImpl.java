package com.doer.moodle.orm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.doer.moodle.common.dao.hibernate.BaseDaoImpl;
import com.doer.moodle.orm.dao.intf.IUserDao;
import com.doer.moodle.orm.entity.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(){
		return this.getSession()
				.createQuery("FROM User")
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> search(String key) {
		return this.getSession()
				.createQuery("FROM User where name like '%"+key+"%'")
				.list();
	}
}

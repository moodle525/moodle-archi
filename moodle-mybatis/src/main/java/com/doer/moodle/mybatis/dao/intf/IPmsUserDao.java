package com.doer.moodle.mybatis.dao.intf;

import com.doer.moodle.mybatis.dao.IBaseDao;
import com.doer.moodle.mybatis.entity.PmsUser;

/**
 * 
 * 用户表数据访问层接口.
 */
public interface IPmsUserDao extends IBaseDao<PmsUser> {

	/**
	 * 根据用户登录名获取用户信息.
	 * 
	 * @param loginName
	 *            .
	 * @return user .
	 */
	PmsUser findByUserNo(String userNo);

}

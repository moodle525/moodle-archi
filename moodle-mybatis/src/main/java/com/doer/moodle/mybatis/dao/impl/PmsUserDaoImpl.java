package com.doer.moodle.mybatis.dao.impl;

import org.springframework.stereotype.Repository;

import com.doer.moodle.common.dao.mybatis.BaseDaoImpl;
import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;
import com.doer.moodle.mybatis.entity.PmsUser;

/**
 * 
 * 用户表数据访问层接口实现类.
 */
@Repository
public class PmsUserDaoImpl extends BaseDaoImpl<PmsUser> implements IPmsUserDao {

	/**
	 * 根据用户登录名获取用户信息.
	 * 
	 * @param loginName
	 *            .
	 * @return user .
	 */

	public PmsUser findByUserNo(String userNo) {
		return this.sqlSession.selectOne(getStatement("findByUserNo"), userNo);
	}

}

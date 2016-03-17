package com.doer.moodle.mybatis.dao.impl;

import org.springframework.stereotype.Repository;

import com.doer.moodle.mybatis.dao.BaseDaoImpl;
import com.doer.moodle.mybatis.dao.entity.PmsUser;
import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;


/**
 * 
 * @描述: 用户表数据访问层接口实现类.
 * @作者: WuShuicheng .
 * @创建时间: 2013-7-22,下午5:51:47 .
 * @版本: 1.0 .
 */
@Repository("pmsUserDao")
public class PmsUserDaoImpl extends BaseDaoImpl<PmsUser> implements IPmsUserDao {

	/**
	 * 根据用户登录名获取用户信息.
	 * 
	 * @param loginName
	 *            .
	 * @return user .
	 */

	public PmsUser findByUserNo(String userNo) {
		return super.getSqlSession().selectOne(getStatement("findByUserNo"), userNo);
	}

}

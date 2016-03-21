package com.doer.moodle.dubbo.impl;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.doer.moodle.common.dao.page.PageBean;
import com.doer.moodle.common.dao.page.PageParam;
import com.doer.moodle.dubbo.interfaces.IPmsUserService;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;
import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;
import com.doer.moodle.mybatis.entity.PmsUser;

@Transactional(rollbackFor = Exception.class)
public class PmsUserServiceImpl implements IPmsUserService {
	@Autowired
	private IPmsUserDao iPmsUserDao;

	/**
	 * 保存用户信息.
	 * 
	 * @param pmsUser
	 */
	@Override
	public void create(PmsUserInfo pmsUser) {
		PmsUser user = new PmsUser();
		propertiesCopy(user, pmsUser);
		iPmsUserDao.insert(user);
	}

	/**
	 * 根据ID获取用户信息.
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public PmsUserInfo getById(Long userId) {
		PmsUser user = iPmsUserDao.getById(userId);
		PmsUserInfo pmsUserInfo = new PmsUserInfo();
		propertiesCopy(pmsUserInfo, user);
		return pmsUserInfo;
	}

	/**
	 * 根据登录名取得用户对象
	 */
	@Override
	public PmsUserInfo findUserByUserNo(String userNo) {
		PmsUser user = iPmsUserDao.findByUserNo(userNo);
		PmsUserInfo pmsUserInfo = new PmsUserInfo();
		propertiesCopy(pmsUserInfo, user);
		return pmsUserInfo;
	}

	/**
	 * 根据ID删除一个用户，同时删除与该用户关联的角色关联信息. type="1"的超级管理员不能删除.
	 * 
	 * @param id
	 *            用户ID.
	 */
	@Override
	public void deleteUserById(long userId) {
		PmsUser pmsUser = iPmsUserDao.getById(userId);
		if (pmsUser != null) {
			if ("1".equals(pmsUser.getUserType())) {
				throw new RuntimeException("【" + pmsUser.getUserNo() + "】为超级管理员，不能删除！");
			}
			iPmsUserDao.deleteById(pmsUser.getId());
		}
	}

	/**
	 * 更新用户信息.
	 * 
	 * @param user
	 */
	@Override
	public void update(PmsUserInfo pmsUserInfo) {
		PmsUser user = new PmsUser();
		propertiesCopy(user, pmsUserInfo);
		iPmsUserDao.update(user);
	}

	/**
	 * 根据用户ID更新用户密码.
	 * 
	 * @param userId
	 * @param newPwd
	 *            (已进行SHA1加密)
	 */
	@Override
	public void updateUserPwd(Long userId, String newPwd, boolean isTrue) {
		PmsUser pmsUser = iPmsUserDao.getById(userId);
		pmsUser.setUserPwd(newPwd);
		pmsUser.setPwdErrorCount(0); // 密码错误次数重置为0
		pmsUser.setIsChangedPwd(isTrue); // 设置密码为已修改过
		iPmsUserDao.update(pmsUser);
	}

	/**
	 * 查询并分页列出用户信息.
	 * 
	 * @param pageParam
	 * @param paramMap
	 * @return
	 */
	@Override
	public PageBean listPage(PageParam pageParam, Map<String, Object> paramMap) {
		return iPmsUserDao.listPage(pageParam, paramMap);
	}

	public void propertiesCopy(Object dest, Object orig) {
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

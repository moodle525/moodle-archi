package com.doer.moodle.dubbo.impl;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.doer.moodle.common.page.PageBean;
import com.doer.moodle.common.page.PageParam;
import com.doer.moodle.dubbo.interfaces.IPmsUserService;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;
import com.doer.moodle.mybatis.dao.intf.IPmsUserDao;
import com.doer.moodle.mybatis.entity.PmsUser;

@Service
@Transactional(rollbackFor = Exception.class)
public class PmsUserServiceImpl implements IPmsUserService {
	@Autowired
	private IPmsUserDao pmsUserDao;

	/**
	 * 保存用户信息.
	 * 
	 * @param pmsUser
	 */
	@Override
	public void create(PmsUserInfo pmsUser) {
		PmsUser user = new PmsUser();
		propertiesCopy(user, pmsUser);
		pmsUserDao.insert(user);
	}

	/**
	 * 根据ID获取用户信息.
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public PmsUserInfo getById(Long userId) {
		PmsUser user = pmsUserDao.getById(userId);
		PmsUserInfo pmsUserInfo = new PmsUserInfo();
		propertiesCopy(pmsUserInfo, user);
		return pmsUserInfo;
	}

	/**
	 * 根据登录名取得用户对象
	 */
	@Override
	public PmsUserInfo findUserByUserNo(String userNo) {
		PmsUser user = pmsUserDao.findByUserNo(userNo);
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
		PmsUser pmsUser = pmsUserDao.getById(userId);
		if (pmsUser != null) {
			if ("1".equals(pmsUser.getUserType())) {
				throw new RuntimeException("【" + pmsUser.getUserNo() + "】为超级管理员，不能删除！");
			}
			pmsUserDao.deleteById(pmsUser.getId());
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
		pmsUserDao.update(user);
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
		PmsUser pmsUser = pmsUserDao.getById(userId);
		pmsUser.setUserPwd(newPwd);
		pmsUser.setPwdErrorCount(0); // 密码错误次数重置为0
		pmsUser.setIsChangedPwd(isTrue); // 设置密码为已修改过
		pmsUserDao.update(pmsUser);
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
		return pmsUserDao.listPage(pageParam, paramMap);
	}

	public void propertiesCopy(Object dest, Object orig) {
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

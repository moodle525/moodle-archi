package com.doer.moodle.dubbo.interfaces;

import java.util.Map;

import com.doer.moodle.common.dao.page.PageBean;
import com.doer.moodle.common.dao.page.PageParam;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;

public interface IPmsUserService {

	void create(PmsUserInfo pmsUser);

	PmsUserInfo getById(Long userId);

	PmsUserInfo findUserByUserNo(String userNo);

	void deleteUserById(long userId);

	void update(PmsUserInfo pmsUserInfo);

	void updateUserPwd(Long userId, String newPwd, boolean isTrue);

	PageBean listPage(PageParam pageParam, Map<String, Object> paramMap);



}

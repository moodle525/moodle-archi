package com.doer.moodle.web.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.config.annotation.Reference;
import com.doer.moodle.common.contants.SessionConstant;
import com.doer.moodle.dubbo.interfaces.IPmsUserService;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;
import com.doer.moodle.web.enums.UserStatusEnum;
import com.doer.moodle.web.enums.UserTypeEnum;

@RequestMapping(value = "/login")
@Controller
public class LoginController {
	private static Logger log = Logger.getLogger(LoginController.class);

	@Reference
	private IPmsUserService iPmsUserService;

	@RequestMapping(value = "/page")
	public String login() {
		return "/login/login";
	}

	@RequestMapping(value = "/do",method=RequestMethod.POST)
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userNo = request.getParameter("userNo");
			if (StringUtils.isEmpty(userNo)) {
				request.setAttribute("userNoMsg", "用户名不能为空");
				return "/login/login";
			}
			request.setAttribute("userNo", userNo);
			PmsUserInfo pmsUserInfo = iPmsUserService.findUserByUserNo(userNo);
			if (pmsUserInfo == null) {
				log.warn("== no such user");
				request.setAttribute("userNoMsg", "用户名或密码不正确");
				return "/login/login";
			}

			if (pmsUserInfo.getStatus().intValue() == UserStatusEnum.INACTIVE.getValue()) {
				log.warn("== 帐号【" + userNo + "】已被冻结");
				request.setAttribute("userNoMsg", "该帐号已被冻结");
				return "/login/login";
			}
			String pwd = request.getParameter("userPwd");
			if (StringUtils.isEmpty(pwd)) {
				request.setAttribute("userPwdMsg", "密码不能为空");
				return "/login/login";
			}

			// 加密明文密码
			// 验证密码
			if (pmsUserInfo.getUserPwd().equals(DigestUtils.sha1Hex(pwd))) {
				// 用户信息，包括登录信息和权限
				HttpSession session = request.getSession();
				session.setAttribute(SessionConstant.USER_SESSION_KEY, pmsUserInfo);

				// 将主帐号ID放入Session
				if (UserTypeEnum.MAIN_USER.getValue().equals(pmsUserInfo.getUserType())) {
					session.setAttribute(SessionConstant.MAIN_USER_ID_SESSION_KEY, pmsUserInfo.getId());
				} else if (UserTypeEnum.SUB_USER.getValue().equals(pmsUserInfo.getUserType())) {
					session.setAttribute(SessionConstant.MAIN_USER_ID_SESSION_KEY, pmsUserInfo.getMainUserId());
				} else {
					// 其它类型用户的主帐号ID默认为0
					session.setAttribute(SessionConstant.MAIN_USER_ID_SESSION_KEY, 0L);
				}

				request.setAttribute("userNo", userNo);
				request.setAttribute("lastLoginTime", pmsUserInfo.getLastLoginTime());

				try {
					// 更新登录数据
					pmsUserInfo.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
					pmsUserInfo.setPwdErrorCount(0); // 错误次数设为0
					iPmsUserService.update(pmsUserInfo);

				} catch (Exception e) {
					request.setAttribute("errorMsg", e.getMessage());
					return "/login/login";
				}

				// 判断用户是否重置了密码，如果重置，弹出强制修改密码页面； TODO
				request.setAttribute("isChangePwd", pmsUserInfo.getIsChangedPwd());

				return "/main/main";

			} else {
				// 密码错误
				log.warn("== wrongPassword");
				// 错误次数加1
				Integer pwdErrorCount = pmsUserInfo.getPwdErrorCount();
				if (pwdErrorCount == null) {
					pwdErrorCount = 0;
				}
				pmsUserInfo.setPwdErrorCount(pwdErrorCount + 1);
				pmsUserInfo.setPwdErrorTime(new Date()); // 设为当前时间
				String msg = "";
				if (pmsUserInfo.getPwdErrorCount().intValue() >= SessionConstant.WEB_PWD_INPUT_ERROR_LIMIT) {
					// 超5次就冻结帐号
					pmsUserInfo.setStatus(UserStatusEnum.INACTIVE.getValue());
					msg += "<br/>密码已连续输错【" + SessionConstant.WEB_PWD_INPUT_ERROR_LIMIT + "】次，帐号已被冻结";
				} else {
					msg += "<br/>密码错误，再输错【"
							+ (SessionConstant.WEB_PWD_INPUT_ERROR_LIMIT - pmsUserInfo.getPwdErrorCount().intValue())
							+ "】次将冻结帐号";
				}
				iPmsUserService.update(pmsUserInfo);
				request.setAttribute("userPwdMsg", msg);
				return "/login/login";

			}
		} catch (RuntimeException e) {
			log.error("login exception:", e);
			request.setAttribute("errorMsg", "登录出错");
			return "/login/login";
		} catch (Exception e) {
			log.error("login exception:", e);
			request.setAttribute("errorMsg", "登录出错");
			return "/login/login";
		}
	}

}

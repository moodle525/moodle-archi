package com.doer.moodle.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.doer.moodle.common.web.BaseController;
import com.doer.moodle.dubbo.interfaces.IPmsUserService;
import com.doer.moodle.dubbo.interfaces.entity.PmsUserInfo;
import com.doer.moodle.web.common.themes.dwz.DwzParam;
import com.doer.moodle.web.enums.UserStatusEnum;
import com.doer.moodle.web.enums.UserTypeEnum;

@Controller
@RequestMapping(value="/account")
public class AccountController extends BaseController{
	private static Logger log = Logger.getLogger(AccountController.class);

	@Reference
	private IPmsUserService iPmsUserService;
	
	@RequestMapping(value="/details")
	public String details(HttpServletRequest request){
		try {
			PmsUserInfo pmsUserInfoInSession = (PmsUserInfo) this.getLoginUser();
			if (pmsUserInfoInSession == null) {
				return operateError("无法从会话中获取用户信息");
			}

			PmsUserInfo pmsUserInfo = iPmsUserService.getById(pmsUserInfoInSession.getId());
			if (pmsUserInfo == null) {
				return operateError("无法获取用户信息");
			}

			request.setAttribute("pmsUserInfo",pmsUserInfo);
			request.setAttribute("UserStatusEnumList", UserStatusEnum.values());
			request.setAttribute("UserStatusEnum", UserStatusEnum.toMap());
			request.setAttribute("UserTypeEnumList", UserTypeEnum.values());
			request.setAttribute("UserTypeEnum", UserTypeEnum.toMap());

			return "/user/pmsUserView";
		} catch (Exception e) {
			log.error("== editPmsUser exception:", e);
			return operateError("无法获取要修改的用户信息失败");
		}
	}

	private String operateError(String message) {
		ajaxDone("300", message);
		return "operateError";
	}
	
	/**
	 * 响应DWZ的Ajax请求.
	 * 
	 * @author WuShuicheng.
	 * @param statusCode
	 *            statusCode:{ok:200, error:300, timeout:301}.
	 * @param message
	 *            提示消息.
	 */
	private void ajaxDone(String statusCode, String message) {
		DwzParam param = getDwzParam(statusCode, message);
		request.setAttribute("DwzParam", param);
	}
	
	/**
	 * 根据request对象，获取页面提交过来的DWZ框架的AjaxDone响应参数值.
	 * 
	 * @author WuShuicheng.
	 * @param statusCode
	 *            状态码.
	 * @param message
	 *            操作结果提示消息.
	 * @return DwzParam :封装好的DwzParam对象 .
	 */
	public DwzParam getDwzParam(String statusCode, String message) {
		// 获取DWZ Ajax响应参数值,并构造成参数对象
		String navTabId = this.request.getParameter("navTabId");
		String dialogId = this.request.getParameter("dialogId");
		String callbackType = this.request.getParameter("callbackType");
		String forwardUrl = this.request.getParameter("forwardUrl");
		String rel = this.request.getParameter("rel");
		return new DwzParam(statusCode, message, navTabId, dialogId, callbackType, forwardUrl, rel, null);
	}
}

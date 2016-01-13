package com.doer.moodle.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doer.moodle.common.entity.R;
import com.doer.moodle.common.entity.wechat.AuthenticInfo;
import com.doer.moodle.common.util.SignUtil;

@Controller
@RequestMapping(value = "/wechat")
public class WeChatController {
	//private static final Logger log = Logger.getLogger(WeChatController.class);

	@RequestMapping(value = "/authentication")
	@ResponseBody
	public R<String> authentication(@RequestBody AuthenticInfo authenticInfo) {
		if (SignUtil.checkSignature(authenticInfo.getSignature(),
				authenticInfo.getTimestamp(), authenticInfo.getNonce())) {
			return new R<String>(authenticInfo.getSignature());
		} else {
			return new R<String>("fail:"+authenticInfo.getSignature());
		}

	}
}

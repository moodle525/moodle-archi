package com.doer.moodle.common.sess.session;

import javax.servlet.http.HttpSession;

/**
 * Title: WOEGO <br>
 * Description: 针对LocalCodeUtil 的内容进行处理 进行处理的服务类<br>
 * Date: 2014-11-18 <br>
 * Copyright (c) 2014 AILK <br>
 * 
 * @author yugn
 */
public interface ILocaleCodeHandlerSV {
	
	/**
	 * 基于 session 信息，对 LocaleCodeUtil 的数据进行处理；
	 * 该接口的实现类提供给 LocaleFilter 使用；
	 * @param session
	 * @author yugn
	 */
	public void doHandler(HttpSession session);

}

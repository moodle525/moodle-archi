package com.doer.moodle.common.util;

import com.alibaba.druid.filter.config.ConfigTools;

public final class DruidUtils {
	private DruidUtils(){}
	
	public static String encrypt(String plainText){
		String encrypt = null;
		try {
			  encrypt = ConfigTools.encrypt(plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;
	}
	
	public static void main(String[] args) {
		System.out.println(DruidUtils.encrypt("root"));
	}
}

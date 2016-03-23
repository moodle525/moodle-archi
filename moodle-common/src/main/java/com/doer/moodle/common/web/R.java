package com.doer.moodle.common.web;

import java.io.Serializable;

import com.doer.moodle.common.contants.WebConstant;

/**
 * 返回页面数据包装类
 * 
 * @author lixiongcheng
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class R<T> implements Serializable {
	private String code; // 错误代码
	private String desc; // 错误描述
	private T data;

	/**
	 * 成功，无数据
	 */
	public R() {
		this.code = WebConstant.RType.SUCCESS;
	}

	/**
	 * 成功，有数据
	 * 
	 * @param data
	 */
	public R(T data) {
		this.code = WebConstant.RType.SUCCESS;
		this.data = data;
	}

	/**
	 * 失败，错误信息
	 * 
	 * @param code
	 * @param desc
	 */
	public R(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

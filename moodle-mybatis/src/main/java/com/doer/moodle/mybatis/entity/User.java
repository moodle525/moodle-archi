package com.doer.moodle.mybatis.entity;

import java.sql.Timestamp;

/**
 * 用户模型
 * 
 * @author StarZou
 * @since 2014年7月5日 下午12:07:20
 **/
public class User {
	private int id;

	private String name;

	private String password;

	private Timestamp createTime;

	private Timestamp updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
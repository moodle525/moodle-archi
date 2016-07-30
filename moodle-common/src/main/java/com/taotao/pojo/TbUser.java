package com.taotao.pojo;

import java.util.Date;

/**
  *用户表
  *2016-06-30
**/
public class TbUser {
    /**
     *用户id
     **/
    private Long id;

    /**
     *用户名
     **/
    private String username;

    /**
     *密码，加密存储
     **/
    private String password;

    /**
     *注册手机号
     **/
    private String phone;

    /**
     *注册邮箱
     **/
    private String email;

    /**
     *创建时间
     **/
    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
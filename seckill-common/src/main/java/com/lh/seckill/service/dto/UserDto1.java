package com.lh.seckill.service.dto;

import java.io.Serializable;

public class UserDto1 implements Serializable {
    private long id;
    // 手机号码
    private String phone;
    // 密码
    private String password;
    // 盐
    private String salt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

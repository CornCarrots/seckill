package com.lh.seckill.service.dto;

import java.util.Date;

public class UserDto2 extends UserDto1 {
    // 地址
    private String address;
    // 注册时间
    private Date registerDate;
    // 登录时间
    private Date loginDate;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}

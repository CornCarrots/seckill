package com.lh.seckill.controller.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RegisterVo implements Serializable {

    @NotNull
    private String phone;

    @NotNull
    private String password;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

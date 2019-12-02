package com.lh.seckill.controller.vo;

import com.lh.seckill.annotation.IsPhone;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginVo implements Serializable {
    /**
     * 通过注解的方式校验手机号（JSR303）
     */
    @NotNull
    @IsPhone// 自定义的注解完成手机号的校验
    private String phone;

    @NotNull
    @Length(min = 6)// 长度最小为32
    private String password;

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
}

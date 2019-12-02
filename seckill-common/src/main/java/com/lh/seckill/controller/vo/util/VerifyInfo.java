package com.lh.seckill.controller.vo.util;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * 验证码
 */
public class VerifyInfo implements Serializable {

    private AbstractCaptcha captcha;
    
    private BufferedImage image;
    
    private String code;

    public VerifyInfo(int height, int width) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height);
        this.captcha = lineCaptcha;
        String code = captcha.getCode();
        BufferedImage image = captcha.getImage();
        this.image = image;
        this.code = code;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getResult() {
        return code;
    }

    public void setResult(String code) {
        this.code = code;
    }

    public void create(String path){
        captcha.write(path);
    }

    public boolean isValid(String input){
        return captcha.verify(input);
    }

    @Override
    public String toString() {
        return "VerifyInfo{" +
                "image=" + image +
                ", code='" + code + '\'' +
                '}';
    }
}

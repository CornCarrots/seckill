package com.lh.seckill.controller.vo.cache;

import java.io.Serializable;

public class SkKeyPrefix extends BaseKeyPrefix implements Serializable {

    public SkKeyPrefix(String prefix) {
        super(prefix);
    }

    public SkKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SkKeyPrefix isOver = new SkKeyPrefix("OVER");
    /**
     * 库存为0的商品的前缀
     */
    public static SkKeyPrefix isSkOver = new SkKeyPrefix("SK_OVER");

    /**
     * 秒杀接口随机地址
     */
    public static SkKeyPrefix skPath = new SkKeyPrefix(60 * 5, "sk_path");
    // 验证码5分钟有效
    public static SkKeyPrefix verifyCode = new SkKeyPrefix(300, "verify_code");

    public static SkKeyPrefix verifyResult = new SkKeyPrefix(300, "verify_result");
}

package com.lh.seckill.controller.vo.cache;

import java.io.Serializable;

public class SkUserKeyPrefix extends BaseKeyPrefix  implements Serializable {

    public static final int TOKEN_EXPIRE = 30*60;// 缓存有效时间为30min

    public static final String TOKEN_PREFERFIX = "token";

    public SkUserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SkUserKeyPrefix token = new SkUserKeyPrefix(TOKEN_EXPIRE, TOKEN_PREFERFIX);

    public static SkUserKeyPrefix getUserByPhone = new SkUserKeyPrefix(0, "sk_user_phone");
}

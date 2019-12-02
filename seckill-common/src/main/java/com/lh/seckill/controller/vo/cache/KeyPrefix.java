package com.lh.seckill.controller.vo.cache;

public interface KeyPrefix {

    // key的过期时间
    int getExpireSeconds();

    // key的前缀
    String getPrefix();
}

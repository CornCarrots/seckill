package com.lh.seckill.controller.vo.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseKeyPrefix implements KeyPrefix, Serializable {

    /**
     * 过期时间
     */
    @JsonProperty("expireSeconds")
    int expireSeconds;

    /**
     * 前缀
     */
    @JsonProperty("prefix")
    String prefix;


    /**
     * 默认过期时间为0，即不过期，过期时间受到redis的缓存策略影响
     *
     * @param prefix 前缀
     */
    public BaseKeyPrefix(String prefix) {
        this(0, prefix);
    }


    public BaseKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }

    /**
     * 前缀为模板类的实现类的类名
     *
     * @return
     */
    @Override
    public String getPrefix() {
        String simpleName = getClass().getSimpleName();
        return simpleName + ":" + prefix;
    }
}

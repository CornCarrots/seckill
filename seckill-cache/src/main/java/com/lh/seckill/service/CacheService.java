package com.lh.seckill.service;

import cn.hutool.core.util.StrUtil;
import com.lh.seckill.controller.vo.cache.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    @Autowired
    RedisTemplate redisTemplate;

    public String get(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        String strValue = Objects.requireNonNull(redisTemplate.boundValueOps(realKey).get()).toString();
        return strValue;
    }

    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        if (StrUtil.isEmptyIfStr(value))
            return false;
        String realKey = prefix.getPrefix() + key;
        // 获取key的过期时间
        int expires = prefix.getExpireSeconds();
        if (expires > 0)
            redisTemplate.boundValueOps(realKey).set(value, expires, TimeUnit.SECONDS);
        else
            redisTemplate.boundValueOps(realKey).set(value);
        return true;
    }

    public boolean delete(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return redisTemplate.delete(realKey);
    }

    public boolean exists(KeyPrefix keyPrefix, String key) {
        String realKey = keyPrefix.getPrefix() + key;
        return redisTemplate.hasKey(realKey);
    }

    public long incr(KeyPrefix keyPrefix, String key, int num) {
        String realKey = keyPrefix.getPrefix() + key;
        return redisTemplate.boundValueOps(realKey).increment(num);
    }

    public long decr(KeyPrefix keyPrefix, String key, int sum) {
        String realKey = keyPrefix.getPrefix() + key;
        return redisTemplate.boundValueOps(realKey).increment(sum);
    }

}

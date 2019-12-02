package com.lh.seckill.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
//        return JSONUtil.parseObj(strValue).toBean(tClass);
        return strValue;
    }

    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        if (StrUtil.isEmptyIfStr(value))
            return false;
        String realKey = prefix.getPrefix() + key;
        // 获取key的过期时间
        int expires = prefix.getExpireSeconds();
        String jsonVal = beanToString(value);
        if (expires > 0)
            redisTemplate.boundValueOps(realKey).set(jsonVal, expires, TimeUnit.SECONDS);
        else
            redisTemplate.boundValueOps(realKey).set(jsonVal);
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

    public long incr(KeyPrefix keyPrefix, String key, long num) {
        String realKey = keyPrefix.getPrefix() + key;
        return redisTemplate.boundValueOps(realKey).increment(num);
    }

    public long decr(KeyPrefix keyPrefix, String key, long num) {
        String realKey = keyPrefix.getPrefix() + key;
        return redisTemplate.boundValueOps(realKey).increment(num);
    }

    /**
     * 将对象转换为对应的json字符串
     *
     * @param value 对象
     * @param <T>   对象的类型
     * @return 对象对应的json字符串
     */
    public static <T> String beanToString(T value) {
        if (value == null)
            return null;

        Class<?> clazz = value.getClass();
        /*首先对基本类型处理*/
        if (clazz == int.class || clazz == Integer.class)
            return "" + value;
        else if (clazz == long.class || clazz == Long.class)
            return "" + value;
        else if (clazz == String.class)
            return (String) value;
            /*然后对Object类型的对象处理*/
        else
            return JSONUtil.toJsonStr(value);
    }

}

package com.lh.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheLockService {
    @Autowired
    RedisTemplate redisTemplate;

    private final String LOCK_SUCCESS = "OK";

    private final String SET_IF_NOT_EXIST = "NX";

    private final String SET_WITH_EXPIRE_TIME = "PX";

    public synchronized boolean lock(String key, String requestId, long expire) {
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            Boolean result = connection.set(key.getBytes(), requestId.getBytes(), Expiration.from(expire, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return result;
        });
    }

    public synchronized boolean releaseLock(String key, String requestId) {
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Boolean result = connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(), requestId.getBytes());
            return result;
        });
    }
}

package com.lh.seckill.controller;

import cn.hutool.json.JSONUtil;
import com.lh.seckill.controller.vo.cache.BaseKeyPrefix;
import com.lh.seckill.controller.vo.cache.KeyPrefix;
import com.lh.seckill.controller.vo.cache.ProductKeyPrefix;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.CacheLockService;
import com.lh.seckill.service.CacheService;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CacheController {
    @Autowired
    CacheService cacheService;
    @Autowired
    CacheLockService cacheLockService;

    @PostMapping("/cache/set")
    public Result set(@RequestParam MultiValueMap<String, Object> params) {
        KeyPrefix prefix = JSONUtil.parseObj(params.getFirst("prefix")).toBean(ProductKeyPrefix.class);
        String key = String.valueOf(params.getFirst("key"));
        String value = String.valueOf(params.getFirst("value"));
//        ParamUtil.check(prefix,key,value);
        cacheService.set(prefix, key, value);
        return Result.success(CodeMsg.SUCCESS);
    }

    @PostMapping("/cache/get")
    public Result<String> get(@RequestParam String key, @RequestParam KeyPrefix prefix) {
        ParamUtil.check(prefix,key);
        String res = cacheService.get(prefix, key);
        return Result.success(res);
    }
    @GetMapping("/cache/exists")
    public Result<Boolean> exists(@RequestParam String key, @RequestParam KeyPrefix prefix) {
        ParamUtil.check(prefix,key);
        Boolean res = cacheService.exists(prefix, key);
        return Result.success(res);
    }

    @PutMapping("/cache/incr")
    public Result incr(@RequestParam String key,@RequestParam String sum, @RequestParam KeyPrefix prefix) {
        ParamUtil.check(prefix,  key, sum);
        cacheService.incr(prefix, key, Integer.parseInt(sum));
        return Result.success(CodeMsg.SUCCESS);
    }

    @PutMapping("/cache/decr")
    public Result decr(@RequestParam String key,@RequestParam String sum, @RequestParam KeyPrefix prefix) {
        ParamUtil.check(prefix,  key, sum);
        cacheService.decr(prefix, key, Integer.parseInt(sum));
        return Result.success(CodeMsg.SUCCESS);
    }

    @PostMapping("/cache/lock")
    public Result lock(@RequestParam Map<String,String> map){
        ParamUtil.check(map, "requestId", "key", "expire");
        String key = (String) ParamUtil.convert(map, "key", String.class);
        String requestId = (String) ParamUtil.convert(map, "requestId", String.class);
        long expire = (Long) ParamUtil.convert(map, "expire", Long.class);
        cacheLockService.lock(key, requestId, expire);
        return Result.success(CodeMsg.SUCCESS);
    }

    @PostMapping("/cache/unlock")
    public Result unlock(@RequestParam Map<String,String> map){
        ParamUtil.check(map, "requestId", "key");
        String key = (String) ParamUtil.convert(map, "key", String.class);
        String requestId = (String) ParamUtil.convert(map, "requestId", String.class);
        cacheLockService.releaseLock(key, requestId);
        return Result.success(CodeMsg.SUCCESS);
    }
}
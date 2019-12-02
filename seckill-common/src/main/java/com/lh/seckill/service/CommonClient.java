package com.lh.seckill.service;

import cn.hutool.json.JSONUtil;
import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.cache.KeyPrefix;
import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.dao.po.OrderItem;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommonClient {
    @Autowired
    RestTemplate restTemplate;

    public OrderItemDto1 getOrderItem(long pid, long uid) {
        Result<OrderItemDto1> result = restTemplate.getForObject("http://seckill-order/order/" + pid + "/" + uid, Result.class);
        if (result == null || result.getData() == null)
            return null;
        return JSONUtil.parseObj(result.getData()).toBean(OrderItemDto1.class);
    }

    public ProductVo getProduct(long pid) {
        Result<ProductVo> result = restTemplate.getForObject("http://seckill-product/product/" + pid, Result.class);
        return JSONUtil.parseObj(result.getData()).toBean(ProductVo.class);
    }

    public List<ProductVo> listProduct(){
        Result<List<ProductVo>> result = restTemplate.getForObject("http://localhost:8011/seckillProducts", Result.class);
        List<ProductVo> productVos = JSONUtil.parseArray(result.getData()).toList(ProductVo.class);
        return productVos;
    }

    public void seckill(long pid, long uid, int sum) {
        Map<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("uid", uid);
        map.put("sum", sum);
        restTemplate.postForObject("http://seckill-service/seckill", map, Result.class);
    }

    public void sendMsg(SKMessage msg) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("msg", JSONUtil.toJsonStr(msg));
        restTemplate.postForObject("http://seckill-mq/send",msg, Result.class);
    }

    public void setCache(KeyPrefix prefix,String key, String value) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("value", value);
        map.add("prefix", JSONUtil.toJsonStr(prefix));
        System.out.println(prefix);
        System.out.println(JSONUtil.toJsonStr(prefix));
        restTemplate.postForObject("http://seckill-cache/cache/set", map, Result.class);
    }

    public String getCache(KeyPrefix prefix, String key) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("prefix", prefix);
        ResponseEntity<Result> result  = restTemplate.postForEntity("http://seckill-cache/cache/get", map, Result.class);
        return (String) result.getBody().getData();
    }

    public Boolean existsCache(KeyPrefix prefix, String key) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("prefix", prefix);
        ResponseEntity<Result> result  = restTemplate.postForEntity("http://seckill-cache/cache/exists", map, Result.class);
        return (Boolean) result.getBody().getData();
    }

    public long incrCache(KeyPrefix prefix, String key, String sum) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("prefix", prefix);
        map.add("sum", sum);
        ResponseEntity<Result> result  = restTemplate.postForEntity("http://seckill-cache/cache/incr", map, Result.class);
        return (Long) result.getBody().getData();
    }

    public long decrCache(KeyPrefix prefix, String key, String sum) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("key", key);
        map.add("prefix", prefix);
        map.add("sum", sum);
        ResponseEntity<Result> result  = restTemplate.postForEntity("http://seckill-cache/cache/decr", map, Result.class);
        return (Long) result.getBody().getData();
    }

    public void lockCache(Map<String, String> map) {
        restTemplate.postForLocation("http://seckill-cache/cache/lock", Result.class, map);
    }

    public void unlockCache(Map<String, String> map) {
        restTemplate.postForLocation("http://seckill-cache/cache/unlock", Result.class, map);
    }
}

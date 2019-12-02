package com.lh.seckill.service;

import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.cache.KeyPrefix;
import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CommonService {
    @Autowired
    CommonClient commonClient;

    public OrderItemDto1 getOrderItem(long pid, long uid) {
        return commonClient.getOrderItem(pid, uid);
    }

    public ProductVo getProduct(long pid) {
        return commonClient.getProduct(pid);
    }

    public List<ProductVo> listProduct(){
        return commonClient.listProduct();
    }

    public void seckill(long pid, long uid, int sum) {
        commonClient.seckill(pid, uid, sum);
    }

    public void sendMsg(SKMessage msg) {
        commonClient.sendMsg(msg);
    }

    public void setCache(KeyPrefix prefix,String key, String value) {
        commonClient.setCache(prefix, key, value);
    }

    public boolean existsCache(KeyPrefix prefix, String key) {
        return commonClient.existsCache(prefix, key);
    }

    public String getCache(KeyPrefix prefix, String key) {
        return commonClient.getCache(prefix, key);
    }

    public long incrCache(KeyPrefix prefix, String key, String sum) {
        return commonClient.incrCache(prefix, key, sum);
    }

    public long decrCache(KeyPrefix prefix, String key, String sum) {
        return commonClient.decrCache(prefix, key, sum);
    }

    public void lockCache(Map<String, String> map) {
        commonClient.lockCache(map);
    }

    public void unlockCache(Map<String, String> map) {
        commonClient.unlockCache(map);
    }

}

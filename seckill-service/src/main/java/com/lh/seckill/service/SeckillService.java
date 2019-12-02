package com.lh.seckill.service;

import cn.hutool.crypto.SecureUtil;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lh.seckill.client.OrderClient;
import com.lh.seckill.client.ProductClient;
import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.cache.OrderKeyPrefix;
import com.lh.seckill.controller.vo.cache.ProductKeyPrefix;
import com.lh.seckill.controller.vo.cache.SkKeyPrefix;
import com.lh.seckill.service.dto.OrderItemDto1;
import com.lh.seckill.util.JudgeUtil;
import com.lh.seckill.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SeckillService {
    @Autowired
    ProductClient productClient;
    @Autowired
    OrderClient orderClient;
    @Autowired
    CommonService commonService;
    @Autowired
    CacheService cacheService;

    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    public void seckill(String pid, String uid, Map<String, String> map) throws Exception {
        JudgeUtil.judge(productClient.reduce(pid, map));
        JudgeUtil.judge(orderClient.create(map));
        setStock(pid);
        setSeckillOrder(Long.parseLong(uid), Long.parseLong(pid));
    }

    public List<ProductVo> listSeckillProduct() throws Exception {
        List<ProductVo> productVos = (List<ProductVo>) JudgeUtil.judge(productClient.listSeckill()).getData();
        return productVos;
    }

    public String randomPath(long uid, long pid) throws Exception {
        String uuid = UuidUtil.uuid() + "123456";
        String path = SecureUtil.md5(uuid);
        setPath(path, uid, pid);
        return path;
    }


    public boolean getOver(long pid) {
        return cacheService.exists(SkKeyPrefix.isSkOver, "_" + pid);
    }

    public boolean isExistsOrder(String uid, String pid) throws Exception {
        boolean found = cacheService.exists(OrderKeyPrefix.skOrderByUidPid, "_" + uid + "_" + pid);
        if (found)
            return true;
        Object result = orderClient.getOrderItem(pid, uid).getData();
        return !Objects.isNull(result);
    }

    public void setStock(String pid) throws Exception {
        ProductVo product = (ProductVo) (JudgeUtil.judge(productClient.get(pid)).getData());
        cacheService.set(ProductKeyPrefix.skProductStock, "_" + pid, String.valueOf(product.getKillStock()));
        if (product.getKillStock() <= 0)
            setOver(Long.parseLong(pid));
    }

    public long getSeckillResult(String userId, String goodsId) throws Exception {
        OrderItemDto1 order = orderClient.getOrderItem(goodsId, userId).getData();
        if (order != null) {//秒杀成功
            return order.getOid();
        } else {
            boolean isOver = getOver(Long.parseLong(goodsId));
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void setPath(String path, long uid, long pid) {
        cacheService.set(SkKeyPrefix.skPath, "_" + uid + "_" + pid, path);
    }

    public String getPath(String uid, String pid) {
        long uuid = Long.parseLong(uid);
        long ppid = Long.parseLong(pid);
        boolean found = cacheService.exists(SkKeyPrefix.skPath, "_" + uuid + "_" + ppid);

        if (found)
            return cacheService.get(SkKeyPrefix.skPath, "_" + uuid + "_" + ppid);
        return null;
    }

    public long preReduce(String pid, String sum) {
        return cacheService.decr(ProductKeyPrefix.skProductStock, "_" + Long.parseLong(pid), Long.parseLong(sum));
    }

    public long postReduce(String pid, String sum) {
        return cacheService.incr(ProductKeyPrefix.skProductStock, "_" + Long.parseLong(pid), 0 - Long.parseLong(sum));
    }

    public void setOver(long pid) {
        cacheService.set(SkKeyPrefix.isSkOver, "_" + pid, "true");
    }

    public void setSeckillOrder(long uid, long pid) {
        cacheService.set(OrderKeyPrefix.skOrderByUidPid, "_" + uid + "_" + pid, "true");
    }
}

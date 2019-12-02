package com.lh.seckill.service;

import com.lh.seckill.config.MQConfig;
import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.cache.OrderKeyPrefix;
import com.lh.seckill.controller.vo.cache.SkKeyPrefix;
import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConsumerService {
    private static Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    CommonService commonService;

    @Autowired
    CacheService cacheService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSkInfo(SKMessage message) {
        logger.info("MQ receive a message: " + message);

        // 获取秒杀用户信息与商品id
        UserVo user = message.getUser();
        long pid = message.getPid();

        // 获取商品的库存
        ProductVo productVo = commonService.getProduct(pid);
        logger.info("MQ get product: " + productVo);

        Integer stockCount = productVo.getKillStock();
        if (stockCount <= 0) {
            return;
        }

        // 判断是否已经秒杀到了（保证秒杀接口幂等性）
        boolean found = this.getSkOrderByUserIdAndGoodsId(user.getId(), pid);

        if (found) {
            return;
        }

        // 1.减库存 2.写入订单 3.写入秒杀订单
        commonService.seckill(pid, user.getId(), 1);
    }

    private boolean getSkOrderByUserIdAndGoodsId(long uid, long pid) {

        // 从redis中取缓存，减少数据库的访问
        boolean found = cacheService.exists(OrderKeyPrefix.skOrderByUidPid, "_" + uid + "_" + pid);
        if (found)
            return true;
        OrderItemDto1 item = commonService.getOrderItem(pid, uid);
        logger.info("MQ get orderItem: " + item);
        return !Objects.isNull(item);
    }
}

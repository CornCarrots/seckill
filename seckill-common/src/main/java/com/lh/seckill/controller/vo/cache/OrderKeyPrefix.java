package com.lh.seckill.controller.vo.cache;

import java.io.Serializable;

public class OrderKeyPrefix extends BaseKeyPrefix  implements Serializable {

    public OrderKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public OrderKeyPrefix(String prefix) {
        super(prefix);
    }

    // 秒杀订单信息的前缀
    public static OrderKeyPrefix skOrderByUidPid = new OrderKeyPrefix("skorder_uidpid");

    public static OrderKeyPrefix skOrder = new OrderKeyPrefix("sk_order");
}

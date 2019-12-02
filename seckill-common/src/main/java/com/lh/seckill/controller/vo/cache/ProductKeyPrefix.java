package com.lh.seckill.controller.vo.cache;

import java.io.Serializable;

public class ProductKeyPrefix extends BaseKeyPrefix  implements Serializable {

    public ProductKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 缓存在redis中的商品库存的前缀(缓存过期时间为永久)
    public static ProductKeyPrefix skProductStock = new ProductKeyPrefix(0, "sk_product_stock");

    public static ProductKeyPrefix productStock = new ProductKeyPrefix(0, "product_stock");
}

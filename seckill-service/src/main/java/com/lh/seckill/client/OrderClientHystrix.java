package com.lh.seckill.client;

import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class OrderClientHystrix implements OrderClient {
    @Override
    public Result create(Map<String, String> map) {
        return Result.error(CodeMsg.MODULE_FAIL.fillArgs("订单"));
    }

    @Override
    public Result<OrderItemDto1> getOrderItem(String pid, String uid) throws Exception {
        return Result.error(CodeMsg.MODULE_FAIL.fillArgs("订单"));
    }
}

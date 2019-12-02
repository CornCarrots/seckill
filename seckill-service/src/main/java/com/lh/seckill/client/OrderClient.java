package com.lh.seckill.client;

import com.lh.seckill.result.Result;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "seckill-order",fallback = OrderClientHystrix.class)
@Component
public interface OrderClient {
    @PostMapping("/order/pay")
    public Result create(@RequestParam Map<String, String> map);

    @GetMapping("/order/{pid}/{uid}")
    public Result<OrderItemDto1> getOrderItem(@PathVariable("pid") String pid, @PathVariable("uid") String uid) throws Exception;
}

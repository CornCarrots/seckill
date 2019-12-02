package com.lh.seckill.controller;

import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.OrderService;
import com.lh.seckill.service.dto.OrderItemDto1;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/pay")
    public Result createOrder(@RequestParam Map<String, String> map) throws Exception {
        ParamUtil.check(map, "uid", "pid", "sum");
        Long uid = (Long) ParamUtil.convert(map, "uid", Long.class);
        Long pid = (Long) ParamUtil.convert(map, "pid", Long.class);
        Integer sum = (Integer) ParamUtil.convert(map, "sum", Integer.class);
        orderService.createSeckillOrder(uid, pid, sum);
        return Result.success(CodeMsg.SUCCESS);
    }

    @GetMapping("/order/{pid}/{uid}")
    public Result<OrderItemDto1> getOrderItem(@PathVariable("pid") String pid, @PathVariable("uid") String uid) throws Exception {
        ParamUtil.check(pid, uid);
        Long pids = (Long) ParamUtil.convert(pid, Long.class);
        Long uids = (Long) ParamUtil.convert(uid, Long.class);
        OrderItemDto1 orderItemDto1 = orderService.getOrderItem(pids, uids);
        if (orderItemDto1 == null)
            return Result.error(CodeMsg.ORDERITEM_NOT_EXIST);
        return Result.success(orderItemDto1);
    }
}

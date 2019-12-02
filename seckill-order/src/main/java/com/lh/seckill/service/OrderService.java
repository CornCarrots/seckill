package com.lh.seckill.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lh.seckill.dao.OrderMapper;
import com.lh.seckill.dao.po.Order;
import com.lh.seckill.dao.po.OrderItem;
import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.service.dto.OrderItemDto1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional
    public void createSeckillOrder(long uid, long pid, int sum) throws Exception{
        Order order = new Order();
        order.setUid(uid);
        orderMapper.createOrder(order);
        long oid = order.getId();
        if (oid < 0)
            throw new GlobalException(CodeMsg.INSERT_FAIL.fillArgs("订单"));
        int oiid = orderMapper.createOrderItem(oid, uid, pid, sum);
        if (oiid < 0)
            throw new GlobalException(CodeMsg.INSERT_FAIL.fillArgs("订单项"));
    }

    public OrderItemDto1 getOrderItem(long pid, long uid) throws Exception{
        OrderItem orderItem = orderMapper.getOrderItem(pid, uid);
        if (orderItem == null)
            return null;
        OrderItemDto1 orderItemDto1 = new OrderItemDto1();
        BeanUtils.copyProperties(orderItem, orderItemDto1);
        return orderItemDto1;
    }


}

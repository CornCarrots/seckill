package com.lh.seckill.service.dto;

import com.lh.seckill.dao.po.OrderItem;

import java.util.List;

public class OrderDto6 extends OrderDto4{

    List<OrderItem> orderItems;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}

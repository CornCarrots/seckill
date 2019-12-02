package com.lh.seckill.service.dto;

import com.lh.seckill.dao.po.Order;

public class OrderItemDto4<User extends UserDto1, Product extends ProductDto1> extends OrderItemDto1 {
    private User user;
    private Order order;
    private Product product;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

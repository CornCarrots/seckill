package com.lh.seckill.service.dto;

public class SeckillProductDto4<T extends ProductDto1> extends SeckillProductDto2 {

    private T product;

    public T getProduct() {
        return product;
    }

    public void setProduct(T product) {
        this.product = product;
    }
}

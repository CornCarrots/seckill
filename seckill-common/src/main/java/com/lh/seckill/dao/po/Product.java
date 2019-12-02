package com.lh.seckill.dao.po;

import java.io.Serializable;

/**
 * 商品
 */

public class Product implements Serializable {
    // ID
    private long id;
    // 名字
    private String name;
    // 标题
    private String title;
    // 简介
    private String summary;
    // 库存
    private int stock;
    // 金额
    private double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}

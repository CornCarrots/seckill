package com.lh.seckill.controller.vo;

import java.io.Serializable;
import java.util.Date;

public class ProductVo implements Serializable {
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
    // 秒杀价格
    private double killPrice;
    // 秒杀库存
    private int killStock;
    // 秒杀开始
    private Date startDate;
    // 秒杀结束
    private Date endDate;

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

    public double getKillPrice() {
        return killPrice;
    }

    public void setKillPrice(double killPrice) {
        this.killPrice = killPrice;
    }

    public int getKillStock() {
        return killStock;
    }

    public void setKillStock(int killStock) {
        this.killStock = killStock;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", killPrice=" + killPrice +
                ", killStock=" + killStock +
                '}';
    }
}

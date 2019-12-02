package com.lh.seckill.service.dto;

import java.util.Date;

public class SeckillProductDto2 extends SeckillProductDto1 {

    private double killPrice;

    private int killStock;

    private Date startDate;

    private Date endDate;

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
}

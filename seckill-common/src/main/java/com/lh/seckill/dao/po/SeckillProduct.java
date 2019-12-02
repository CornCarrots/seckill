package com.lh.seckill.dao.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀商品
 */
public class SeckillProduct implements Serializable {
    // ID
    private long id;
    // 商品
    private long pid;
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

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
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
        return "SeckillProduct{" +
                "id=" + id +
                ", pid=" + pid +
                ", killPrice=" + killPrice +
                ", killStock=" + killStock +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

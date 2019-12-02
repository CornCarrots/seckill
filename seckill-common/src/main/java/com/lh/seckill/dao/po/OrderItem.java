package com.lh.seckill.dao.po;

import java.io.Serializable;

public class OrderItem implements Serializable {
    // ID
    private long id;
    // 用户
    private long uid;
    // 商品
    private long pid;
    // 订单
    private long oid;
    // 商品数量
    private int sum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}

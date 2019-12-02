package com.lh.seckill.dao.po;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    // ID
    private long id;
    // 用户
    private long uid;
    // 订单状态
    private String status;
    // 创建时间
    private Date createDate;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}

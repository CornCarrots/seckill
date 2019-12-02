package com.lh.seckill.service.dto;

import java.io.Serializable;

public class SeckillProductDto1 implements Serializable {

    private long id;

    private long pid;

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
}

package com.lh.seckill.controller.vo.mq;

import com.lh.seckill.controller.vo.UserVo;

import java.io.Serializable;

public class SKMessage implements Serializable {
    private UserVo user;

    private long pid;

    public SKMessage() {
    }

    public SKMessage(UserVo user, long pid) {
        this.user = user;
        this.pid = pid;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "SKMessage{" +
                "user=" + user +
                ", pid=" + pid +
                '}';
    }
}

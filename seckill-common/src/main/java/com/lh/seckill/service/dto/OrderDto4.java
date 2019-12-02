package com.lh.seckill.service.dto;


public class OrderDto4<User extends UserDto1> extends OrderDto1{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.lh.seckill.config.access;

import com.lh.seckill.controller.vo.UserVo;

/**
 * 使用ThreadLocal保存用户，因为ThreadLocal是线程安全的，使用ThreadLocal可以保存当前线程持有的对象
 */
public class UserContext {
    private static ThreadLocal<UserVo> userVoThreadLocal = new ThreadLocal<>();
    public static void setUser(UserVo user){
        userVoThreadLocal.set(user);
    }
    public static UserVo getUser(){
        return userVoThreadLocal.get();
    }
}

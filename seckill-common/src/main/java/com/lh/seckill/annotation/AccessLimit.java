package com.lh.seckill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    // 两次请求的最大有效时间间隔，即视两次请求为同一状态的时间间隔
    int seconds();


     // 最大请求次数
    int maxAccessCount();


    // 是否需要重新登录
    boolean needLogin() default true;
}

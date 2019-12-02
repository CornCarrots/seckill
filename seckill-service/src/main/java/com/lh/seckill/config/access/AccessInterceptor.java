package com.lh.seckill.config.access;

import cn.hutool.json.JSONUtil;
import com.lh.seckill.annotation.AccessLimit;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.cache.AccessKeyPrefix;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.CacheService;
import com.lh.seckill.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    @Autowired
    CacheService cacheService;

    /**
     * 目标方法执行前的处理
     * <p>
     * 查询访问次数，进行防刷请求拦截
     * 在 AccessLimit#seconds() 时间内频繁访问会有次数限制
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(request.getRequestURL() + " 拦截请求");

        // 指明拦截的是方法
        if (handler instanceof HandlerMethod) {
            logger.info("HandlerMethod: " + ((HandlerMethod) handler).getMethod().getName());
            // 获取用户对象
            UserVo user = CookieUtil.getUser(request, response,cacheService);
            // 保存用户到ThreadLocal，这样，同一个线程访问的是同一个用户
//            UserContext.setUser(user);

            // 获取标注了 @AccessLimit 的方法，没有注解，则直接返回
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);

            // 如果没有添加@AccessLimit注解，直接放行（true）
            if (accessLimit == null)
                return true;

            // 获取注解的元素值
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxAccessCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getPhone();
            } else {
                //do nothing
            }
            // 设置缓存过期时间
            AccessKeyPrefix accessKeyPrefix = AccessKeyPrefix.withExpire(seconds);
            // 在redis中存储的访问次数的key为请求的URI
            boolean found = cacheService.exists(accessKeyPrefix, key);
            // 第一次重复点击 秒杀按钮
            if (!found)
                cacheService.set(accessKeyPrefix, key, 1);
            else {
                Integer count = Integer.parseInt(cacheService.get(accessKeyPrefix, key));
                // 点击次数未达最大值
                if (count < maxCount) {
                    cacheService.incr(accessKeyPrefix, key, 1);
                    // 点击次数已满
                } else {
                    render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                    return false;
                }
            }
        }
        // 不是方法直接放行
        return true;
    }
    private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSONUtil.toJsonStr(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}

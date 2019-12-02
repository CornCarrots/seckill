package com.lh.seckill.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.cache.SkUserKeyPrefix;
import com.lh.seckill.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public static UserVo getUser(HttpServletRequest request, HttpServletResponse response, CacheService cacheService){

        // 从请求对象中获取token（token可能有两种方式从客户端返回，1：通过url的参数，2：通过set-Cookie字段）
        String token = CookieUtil.getCookie(request, "token");
        logger.info("获取token：" + token);
        // 通过token就可以在redis中查出该token对应的用户对象
        UserVo userVo = null;
        if (token != null && cacheService.exists(SkUserKeyPrefix.token, token))
            userVo = JSONUtil.parseObj(cacheService.get(SkUserKeyPrefix.token, token)).toBean(UserVo.class);

        // 在有效期内从redis获取到key之后，需要将key重新设置一下，从而达到延长有效期的效果
        if (userVo != null) {
            logger.info("获取userVo：" + userVo.toString());
            CookieUtil.addCookie(response, token, userVo, cacheService);
        }
        else
            logger.info("获取userVo为空");
        return userVo;

    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        String token = request.getParameter(cookieName);
        if (StrUtil.isEmptyIfStr(token)){
            token = getCookieValue(request, cookieName);
            if (StrUtil.isEmptyIfStr(token))
                return null;
        }
        return token;
    }
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        logger.info("getCookieValue");
        Cookie[] cookies = request.getCookies();
        // null判断，否则并发时会发生异常
        if (cookies == null || cookies.length == 0) {
            logger.info("cookies is null");
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    public static void addCookie(HttpServletResponse response, String token, UserVo user, CacheService cacheService) {
        cacheService.set(SkUserKeyPrefix.token, token, user);
        Cookie cookie = new Cookie(SkUserKeyPrefix.TOKEN_PREFERFIX, token);
        cookie.setMaxAge(SkUserKeyPrefix.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

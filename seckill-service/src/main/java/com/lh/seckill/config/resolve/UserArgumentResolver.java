package com.lh.seckill.config.resolve;

import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.service.CacheService;
import com.lh.seckill.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static Logger logger = LoggerFactory.getLogger(UserArgumentResolver.class);

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        logger.info("supportsParameter...");
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == UserVo.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 获取请求和响应对象
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        logger.info(request.getRequestURL() + " resolveArgument");
        return CookieUtil.getUser(request,response, cacheService);
    }
}

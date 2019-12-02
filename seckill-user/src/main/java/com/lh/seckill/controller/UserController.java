package com.lh.seckill.controller;

import com.lh.seckill.controller.vo.LoginVo;
import com.lh.seckill.controller.vo.RegisterVo;
import com.lh.seckill.controller.vo.cache.SkUserKeyPrefix;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.UserService;
import com.lh.seckill.service.dto.UserDto2;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("user/login")
    public Result<String> login(@RequestBody @Valid LoginVo loginVo, HttpServletResponse response) throws Exception{
        String token = userService.login(loginVo);
        Cookie cookie = new Cookie(SkUserKeyPrefix.TOKEN_PREFERFIX, token);
        cookie.setMaxAge(SkUserKeyPrefix.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Result.success(token);
    }

    @PutMapping("user/register")
    public Result register(@RequestBody @Valid RegisterVo registerVo) throws Exception{
        userService.register(registerVo);
        return Result.success(CodeMsg.REGISTER_SUCCESS);
    }

    @GetMapping("user/{id}")
    public Result<UserDto2> get(@PathVariable("id") String id) throws Exception{
        ParamUtil.check(id);
        long uid = (long) ParamUtil.convert(id, Long.class);
        UserDto2 userDto2 = userService.getUser(uid);
        return Result.success(userDto2);
    }
}

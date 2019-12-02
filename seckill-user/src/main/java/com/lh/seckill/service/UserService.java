package com.lh.seckill.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lh.seckill.controller.vo.LoginVo;
import com.lh.seckill.controller.vo.RegisterVo;
import com.lh.seckill.controller.vo.cache.SkUserKeyPrefix;
import com.lh.seckill.dao.UserMapper;
import com.lh.seckill.dao.po.User;
import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.service.dto.UserDto1;
import com.lh.seckill.service.dto.UserDto2;
import com.lh.seckill.util.MD5Util;
import com.lh.seckill.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger lg = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CacheLockService lockService;

    public void register(RegisterVo registerVo) throws Exception {
        String unique = UuidUtil.uuid() + "-" + Thread.currentThread().getId();
        String lockKey = "redis-lock" + registerVo.getPhone();
        boolean isLock = lockService.lock(lockKey, unique, 60 * 1000);
        if (!isLock)
            throw new GlobalException(CodeMsg.WAIT_REGISTER_DONE);
        lg.info("注册接口开始加锁");
        if (userMapper.getByPhone(registerVo.getPhone()) != null) {
            lockService.releaseLock(lockKey, unique);
            throw new GlobalException(CodeMsg.USER_EXIST);
        }
        String dbPass = MD5Util.inputPassToDbPass(registerVo.getPassword(), MD5Util.SALT);
        int res = userMapper.insert(registerVo.getPhone(), dbPass, MD5Util.SALT, registerVo.getAddress());
        boolean unLock = lockService.releaseLock(lockKey, unique);
        if (!unLock)
            throw new GlobalException(CodeMsg.REGISTER_FAIL);
        if (res < 1)
            throw new GlobalException(CodeMsg.REGISTER_FAIL);
    }

    public String login(LoginVo loginVo) throws Exception {
        User user = getUserByPhone(loginVo.getPhone());
        if (user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        String dbPass = MD5Util.inputPassToDbPass(loginVo.getPassword(), user.getSalt());
        if (!StrUtil.equals(dbPass, user.getPassword()))
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        String token = UuidUtil.uuid();
        cacheService.set(SkUserKeyPrefix.token, token, user);
        return token;
    }

    public UserDto2 getUser(long uid) throws Exception {
        User user = userMapper.getById(uid);
        if (user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        UserDto2 userDto2 = new UserDto2();
        BeanUtils.copyProperties(user, userDto2);
        return userDto2;
    }

    public User getUserByPhone(String phone) throws Exception {
        User user = null;
        if (cacheService.exists(SkUserKeyPrefix.getUserByPhone, "_" + phone))
            user = JSONUtil.parseObj(cacheService.get(SkUserKeyPrefix.getUserByPhone, "_" + phone)).toBean(User.class);
        if (user == null)
            user = userMapper.getByPhone(phone);
        if (user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        cacheService.set(SkUserKeyPrefix.getUserByPhone, "_" + phone, user);
        return user;
    }
}

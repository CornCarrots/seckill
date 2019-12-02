package com.lh.seckill.util;

import cn.hutool.core.lang.Validator;

public class ValidatorUtil {
    public static boolean isMobile(String mobile) {
        return Validator.isMobile(mobile);
    }
}

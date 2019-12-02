package com.lh.seckill.util;

import cn.hutool.core.util.IdUtil;

public class UuidUtil {
    public static String uuid() {

        return IdUtil.fastSimpleUUID().replace("-", "");
    }
}

package com.lh.seckill.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamUtil {
    public static void check(Map<String, String> map,String...args){
        for (String arg: args) {
            String argStr = StrUtil.trimToNull(map.get(arg));
            if (StrUtil.isEmptyIfStr(argStr))
                throw new GlobalException(CodeMsg.BIND_ERROR.fillArgs(arg,argStr));
        }
    }
    public static void check(Object...args){
        for (Object arg: args) {
            if (arg == null)
                throw new GlobalException(CodeMsg.BIND_ERROR.fillArgs(arg));
            if (arg.getClass() == String.class){
                String argStr = StrUtil.trimToNull(arg.toString());
                if (StrUtil.isEmptyIfStr(argStr))
                    throw new GlobalException(CodeMsg.BIND_ERROR.fillArgs(arg));
            }
        }
    }

    public static Object convert(String param, Class clazz){
        String str = StrUtil.trimToNull(param);
        return Convert.convert(clazz, str);
    }
    public static Object convert(Map<String, String> map, String param, Class clazz){
        String str = map.get(param);
        return convert(str, clazz);
    }

    public static HashMap<String,String> toMap(List<String> keys, Object...args){
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), StrUtil.toString(args[i]));
        }
        return map;
    }
}

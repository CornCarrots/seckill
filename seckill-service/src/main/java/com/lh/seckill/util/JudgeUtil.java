package com.lh.seckill.util;

import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;

public class JudgeUtil {

    public static Result judge(Result result)throws Exception{
        if (result.getCode() != CodeMsg.SUCCESS.getCode())
            throw new GlobalException(result.getCodeMsg());
        return result;
    }
}

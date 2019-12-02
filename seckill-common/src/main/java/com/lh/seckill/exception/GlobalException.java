package com.lh.seckill.exception;

import com.lh.seckill.result.CodeMsg;

/**
 * 全局异常
 */
public class GlobalException  extends RuntimeException {
    private CodeMsg codeMsg;

    /**
     * 使用构造器接收CodeMsg
     *
     * @param codeMsg
     */
    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}

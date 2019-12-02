package com.lh.seckill.result;

import java.io.Serializable;

/**
 * 状态码
 */
public class CodeMsg implements Serializable {
    private int code;
    private String msg;

    /**
     * 通用异常
     */
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数%s校验异常：%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg VERITF_FAIL = new CodeMsg(500103, "校验失败，请重新输入表达式结果或刷新校验码重新输入");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104, "访问太频繁！");
    public static CodeMsg INSERT_FAIL = new CodeMsg(500104, "添加 %s 失败");
    public static CodeMsg DELETE_FAIL = new CodeMsg(500104, "删除 %s 失败");
    public static CodeMsg UPDATE_FAIL = new CodeMsg(500104, "更新 %s 失败");
    public static CodeMsg GET_FAIL = new CodeMsg(500104, "查找 %s 失败");
    public static CodeMsg MODULE_FAIL = new CodeMsg(500105, " %s 模块出现异常");

    /**
     * 用户模块 5002XX
     */
    public static CodeMsg SESSION_ERROR = new CodeMsg(500201, "Session不存在或者已经失效，请返回登录！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500202, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500203, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500204, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500205, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500206, "密码错误");
    public static CodeMsg USER_EXIST = new CodeMsg(500207, "用户已经存在，无需重复注册");
    public static CodeMsg REGISTER_SUCCESS = new CodeMsg(500208, "注册成功");
    public static CodeMsg REGISTER_FAIL = new CodeMsg(500209, "注册异常");
    public static CodeMsg FILL_REGISTER_INFO = new CodeMsg(500210, "请填写注册信息");
    public static CodeMsg WAIT_REGISTER_DONE = new CodeMsg(500211, "等待注册完成");
    public static CodeMsg LOGIN_SUCCESS = new CodeMsg(500212, "登录成功");

    //商品模块 5003XX
    public static CodeMsg PRODUCT_NOT_EXIST = new CodeMsg(500300, "商品不存在");
    public static CodeMsg PRODUCT_EMPTY = new CodeMsg(500301, "商品列表为空");
    public static CodeMsg SECKILL_PRODUCT_EMPTY = new CodeMsg(500301, "秒杀商品列表为空");
    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");
    public static CodeMsg ORDERITEM_NOT_EXIST = new CodeMsg(500401, "订单项不存在");

    /**
     * 秒杀模块 5005XX
     */
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品库存不足");
    public static CodeMsg REPEATE_SECKILL = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg SECKILL_FAIL = new CodeMsg(500502, "秒杀失败");
    public static CodeMsg SECKILL_PARM_ILLEGAL = new CodeMsg(500503, "秒杀请求参数异常：%s");
    public static CodeMsg SECKILL_SUCCESS = new CodeMsg(500504, "秒杀成功");

    /**
     * 构造器定义为private是为了防止controller直接new
     *
     * @param code
     * @param msg
     */
    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 动态地填充msg字段
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);// 将arg格式化到msg中，组合成一个message
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

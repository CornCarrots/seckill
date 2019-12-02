package com.lh.seckill.client;

import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductClientHystrix implements ProductClient {

    @Override
    public Result reduce(String id, Map<String, String> map) {
        return Result.error(CodeMsg.MODULE_FAIL.fillArgs("商品模块"));
    }

    @Override
    public Result<ProductVo> get(String id) throws Exception {
        return Result.error(CodeMsg.MODULE_FAIL.fillArgs("商品模块"));
    }

    @Override
    public Result<List<ProductVo>> listSeckill() throws Exception {
        return Result.error(CodeMsg.MODULE_FAIL.fillArgs("商品模块"));
    }
}

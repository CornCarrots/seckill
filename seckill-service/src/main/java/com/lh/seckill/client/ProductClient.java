package com.lh.seckill.client;

import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "seckill-product",fallback = ProductClientHystrix.class)
@Component
public interface ProductClient {
    @PutMapping("/product/{id}")
    public Result reduce(@PathVariable("id")String pid, @RequestParam Map<String, String> map);

    @GetMapping("/product/{id}")
    public Result<ProductVo> get(@PathVariable("id") String id) throws Exception;

    @GetMapping("/seckillProducts")
    public Result<List<ProductVo>> listSeckill() throws Exception ;
}

package com.lh.seckill.controller;

import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.ProductService;
import com.lh.seckill.service.dto.ProductDto2;
import com.lh.seckill.service.dto.SeckillProductDto4;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public Result<List<ProductDto2>> list() throws Exception {
        List<ProductDto2> productDto2s = productService.listProduct();
        return Result.success(productDto2s);
    }

    @GetMapping("/seckillProducts")
    public Result<List<ProductVo>> listSeckill() throws Exception {
        List<SeckillProductDto4<ProductDto2>> seckillProductDto4s = productService.listSeckill();
        List<ProductVo> productVos = new ArrayList<>();
        for (SeckillProductDto4<ProductDto2> seckillProductDto4 :
                seckillProductDto4s) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(seckillProductDto4, productVo);
            productVo.setId(seckillProductDto4.getProduct().getId());
            productVo.setName(seckillProductDto4.getProduct().getName());
            productVo.setTitle(seckillProductDto4.getProduct().getTitle());
            productVo.setSummary(seckillProductDto4.getProduct().getSummary());
            productVo.setPrice(seckillProductDto4.getProduct().getPrice());
            productVo.setStock(seckillProductDto4.getProduct().getStock());
            productVos.add(productVo);
        }
        return Result.success(productVos);
    }

    @GetMapping("/product/{id}")
    public Result<ProductVo> get(@PathVariable("id") String id) throws Exception {
        ParamUtil.check(id);
        Long pid = (Long) ParamUtil.convert(id, Long.class);
        SeckillProductDto4<ProductDto2> seckillProductDto4 = productService.getProduct(pid);
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(seckillProductDto4, productVo);
        productVo.setId(seckillProductDto4.getProduct().getId());
        productVo.setName(seckillProductDto4.getProduct().getName());
        productVo.setTitle(seckillProductDto4.getProduct().getTitle());
        productVo.setSummary(seckillProductDto4.getProduct().getSummary());
        productVo.setPrice(seckillProductDto4.getProduct().getPrice());
        productVo.setStock(seckillProductDto4.getProduct().getStock());
        return Result.success(productVo);
    }

    @PutMapping(value = "/product/{id}")
    public Result<ProductVo> reduce(@PathVariable("id")String id, @RequestParam Map<String, String> map) throws Exception {
        ParamUtil.check(map, "sum", "pid");
        int sum = (Integer) ParamUtil.convert(map, "sum", Integer.class);
        long pid = (Long) ParamUtil.convert(map, "pid", Long.class);
        productService.reduceStock(pid, sum);
        return Result.success(CodeMsg.SUCCESS);
    }
}
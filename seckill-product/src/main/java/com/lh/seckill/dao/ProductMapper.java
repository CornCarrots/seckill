package com.lh.seckill.dao;

import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.dao.po.Product;
import com.lh.seckill.dao.po.SeckillProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface ProductMapper {
    // 所有商品
    public List<Product> listProduct();

    public List<SeckillProduct> listSeckillProduct();

    // 指定商品
    public Product getProduct(@Param("pid") long pid);

    public SeckillProduct getSeckillProduct(@Param("spid") long spid);

    public SeckillProduct getSeckillProductByPid(@Param("pid") long pid);

    // 减库存
    public int reduceStock(@Param("pid") long pid, @Param("sum") int sum);
}

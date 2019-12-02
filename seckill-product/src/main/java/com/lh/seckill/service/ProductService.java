package com.lh.seckill.service;

import cn.hutool.core.collection.CollUtil;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.cache.ProductKeyPrefix;
import com.lh.seckill.controller.vo.cache.SkKeyPrefix;
import com.lh.seckill.dao.ProductMapper;
import com.lh.seckill.dao.po.Product;
import com.lh.seckill.dao.po.SeckillProduct;
import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.service.dto.ProductDto2;
import com.lh.seckill.service.dto.SeckillProductDto4;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CacheService cacheService;

    public SeckillProductDto4<ProductDto2> getProduct(long pid) throws Exception{
        Product product =  productMapper.getProduct(pid);
        if (product == null)
            throw new GlobalException(CodeMsg.PRODUCT_NOT_EXIST);
        SeckillProduct seckillProduct = productMapper.getSeckillProductByPid(pid);
        ProductDto2 dto2 = new ProductDto2();
        dto2.setId(product.getId());
        dto2.setName(product.getName());
        dto2.setPrice(product.getPrice());
        dto2.setStock(product.getStock());
        dto2.setSummary(product.getSummary());
        dto2.setTitle(product.getTitle());
        SeckillProductDto4 seckillProductDto4 = new SeckillProductDto4();
        seckillProductDto4.setId(seckillProduct.getId());
        seckillProductDto4.setKillPrice(seckillProduct.getKillPrice());
        seckillProductDto4.setKillStock(seckillProduct.getKillStock());
        seckillProductDto4.setPid(seckillProduct.getPid());
        seckillProductDto4.setStartDate(seckillProduct.getStartDate());
        seckillProductDto4.setEndDate(seckillProduct.getEndDate());
        seckillProductDto4.setProduct(dto2);
        return seckillProductDto4;
    }

    public List<ProductDto2> listProduct() throws Exception{
        List<Product> products = productMapper.listProduct();
        if (products.size() == 0)
            throw new GlobalException(CodeMsg.PRODUCT_EMPTY);
        List<ProductDto2> productDto2s = new ArrayList<>();
        for (Product product: products) {
            ProductDto2 dto2 = new ProductDto2();
            BeanUtils.copyProperties(product, dto2);
            productDto2s.add(dto2);
        }
        return productDto2s;
    }

    public List<SeckillProductDto4<ProductDto2>> listSeckill() throws Exception{
        List<SeckillProduct> seckillProducts = productMapper.listSeckillProduct();
        if (seckillProducts.size() == 0)
            throw new GlobalException(CodeMsg.SECKILL_PRODUCT_EMPTY);
        List<SeckillProductDto4<ProductDto2>> seckillProductDto4s = new ArrayList<>();
        for (SeckillProduct seckillProduct: seckillProducts) {
            long pid = seckillProduct.getId();
            Product product = productMapper.getProduct(pid);
            ProductDto2 dto2 = new ProductDto2();
            BeanUtils.copyProperties(product, dto2);
            SeckillProductDto4 dto4 = new SeckillProductDto4();
            BeanUtils.copyProperties(seckillProduct, dto4);
            dto4.setProduct(dto2);
            seckillProductDto4s.add(dto4);
            setStock(pid, seckillProduct.getKillStock());
        }
        return seckillProductDto4s;
    }

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional
    public void reduceStock(long pid, int sum){
        int res =  productMapper.reduceStock(pid, sum);
        if (res != 1)
            throw new GlobalException(CodeMsg.SECKILL_OVER);
    }

    public void setOver(long pid){
        cacheService.set(SkKeyPrefix.isSkOver,"_" + pid,"true");
    }

    public void setStock(long pid, int stock) throws Exception {
        cacheService.set(ProductKeyPrefix.skProductStock,"_" + pid, stock);
    }
}

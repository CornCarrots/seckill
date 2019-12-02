package com.lh.seckill.dao;

import com.lh.seckill.dao.po.Order;
import com.lh.seckill.dao.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper {
    public Order getOrder(@Param("id") long id);

    public OrderItem getOrderItem(@Param("pid") long pid, @Param("uid") long uid);

    public int createOrder(@Param("order") Order order);

    public int createOrderItem(@Param("oid") long oid, @Param("uid") long uid, @Param("pid") long pid, @Param("sum") int sum);

}

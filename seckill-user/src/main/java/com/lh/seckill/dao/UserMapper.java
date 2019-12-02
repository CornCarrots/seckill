package com.lh.seckill.dao;

import com.lh.seckill.dao.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public int insert(@Param("phone") String phone,@Param("password") String password,
                      @Param("salt") String salt, @Param("address") String address);

    public User getByPhone(@Param("phone") String phone);

    public User getById(@Param("uid") long uid);

    public int update(@Param("uid") long uid, @Param("password") String password);
}

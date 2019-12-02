package com.lh.seckill.controller.vo.cache;

import java.io.Serializable;

public class UserKey extends BaseKeyPrefix  implements Serializable {

    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getUserById = new UserKey("user_Id");

    public static UserKey getUserByName = new UserKey("user_Name");
}

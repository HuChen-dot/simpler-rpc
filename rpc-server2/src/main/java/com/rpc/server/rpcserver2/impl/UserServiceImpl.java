package com.rpc.server.rpcserver2.impl;

import com.hu.api.IUserService;
import com.hu.pojo.User;
import org.hu.rpc.annotation.RpcService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/26 10:39 PM
 **/
@RpcService
@Service
public class UserServiceImpl implements IUserService {

    Map<Object, User> userMap = new HashMap();

    @Override
    public User getById(int id) {
        if (userMap.size() == 0) {
            User user1 = new User();
            user1.setId(1);
            user1.setName("服务2：王武");
            User user2 = new User();
            user2.setId(2);
            user2.setName("服务2：赵六");
            userMap.put(user1.getId(), user1);
            userMap.put(user2.getId(), user2);
        }
        return userMap.get(id);
    }
}

package com.hu.client.rpcclient.service.impl;

import com.hu.api.IUserService;
import com.hu.pojo.User;
import org.hu.rpc.annotation.RpcAutowired;
import org.springframework.stereotype.Service;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/26 10:39 PM
 **/
@Service
public class UserServiceImpl {


    @RpcAutowired
    private IUserService iUserService;


    public User getById(int id) {
        return iUserService.getById(id);
    }
}

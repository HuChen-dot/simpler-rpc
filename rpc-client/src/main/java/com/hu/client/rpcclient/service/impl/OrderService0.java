package com.hu.client.rpcclient.service.impl;

import com.hu.api.OrderService;
import org.hu.rpc.annotation.RpcAutowired;
import org.springframework.stereotype.Service;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/30 11:44 AM
 **/
@Service
public class OrderService0 {

    @RpcAutowired
    OrderService orderService;


    public String find(Integer id){
        return orderService.findOrder(id);
    }
}

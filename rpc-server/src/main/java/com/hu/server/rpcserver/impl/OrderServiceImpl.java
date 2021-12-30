package com.hu.server.rpcserver.impl;

import com.hu.api.OrderService;
import org.hu.rpc.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/30 11:42 AM
 **/
@Service
@RpcService
public class OrderServiceImpl implements OrderService {


    @Override
    public String findOrder(Integer id) {
        return "我是服务提供者:"+id;
    }
}

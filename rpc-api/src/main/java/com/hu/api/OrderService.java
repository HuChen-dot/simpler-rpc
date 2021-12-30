package com.hu.api;

import org.hu.rpc.annotation.RpcTag;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/30 11:42 AM
 **/
@RpcTag(dept = "qx",service = "order")
public interface OrderService {

   String findOrder(Integer id);
}

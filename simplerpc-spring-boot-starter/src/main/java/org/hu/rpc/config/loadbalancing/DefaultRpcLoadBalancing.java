package org.hu.rpc.config.loadbalancing;

import java.util.List;

/**
 * @Author: hu.chen
 * @Description: 默认轮询的负载均衡策略
 * @DateTime: 2021/12/27 9:09 PM
 **/
public class DefaultRpcLoadBalancing implements RpcLoadBalancing{

    /**
     * 计数器
     */
    private volatile static int count=0;

    @Override
    public String[] load(List<String[]> services) {
        if(count==services.size()){
            count=0;
        }
        String[] service = services.get(count);
        count++;
        return service;
    }
}

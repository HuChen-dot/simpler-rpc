package org.hu.rpc.config.loadbalancing;

import java.util.List;

/**
 * @Author: hu.chen
 * @Description: 随机的负载均衡算法
 * @DateTime: 2021/12/27 9:12 PM
 **/
public class RandomRpcLoadBalancing implements RpcLoadBalancing{


    /**
     * 随机的负载均衡
     * @param services
     * @return
     */
    @Override
    public String[] load(List<String[]> services) {
        // 此处负载均衡策略为随机
        int i = (int) (System.currentTimeMillis() % services.size());
        return services.get(i);
    }
}
package org.hu.rpc.config;

import org.hu.rpc.config.loadbalancing.*;
import org.hu.rpc.util.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: hu.chen
 * @Description: Netty 配置类
 * @DateTime: 2021/12/26 6:39 PM
 **/
//此注解用来开启下面的@ConfigurationProperties注解，
@EnableConfigurationProperties(NettyClientConfig.class)
//用来读取配置文件中的值，给类的属性自动赋值
@ConfigurationProperties(prefix = "simplerpc.netty.client")
public class NettyClientConfig {

    /**
     * netty 默认连接地址
     * 下面这段配置代表：人事部:订单服务//ip:端口号和ip:端口号|人事部下的用户服务//ip:端口号和ip:端口号,电商部:订单服务//ip:端口号和ip:端口号|电商部下的用户服务//ip:端口号和ip:端口号
     * ministry_of_personne:order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092,electronic:order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092
     */
    private String address = "xhs|oa//127.0.0.1:8091";

    /**
     * 客户端连接超时时间
     */
    private Integer connecttimeout = 3000;

    /**
     * 负载均衡策略
     */
    private String loadbalancing = LoadBalancingConst.POLLING;


    private List<String[]> arrayAddress = null;

    private Map<String, List<String[]>> mapAddress = new ConcurrentHashMap<>();


    public Integer getConnecttimeout() {
        return connecttimeout;
    }

    public void setConnecttimeout(Integer connecttimeout) {
        this.connecttimeout = connecttimeout;
    }


    public String[] getHostAndPort(String path) {
        if (mapAddress.size() == 0) {
            synchronized (this) {
                if (mapAddress.size() == 0) {
// ministry_of_personne|order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092,electronic|order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092
                    String[] depts = address.split(",");
                    for (String dept : depts) {
                        // 用来存储，某一个部门下的某一个服务的集群实例
                        List<String[]> services = new ArrayList<>();
                        // 获取部门名称
                        //ministry_of_personne|order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092
                        String[] arrayServices = dept.split("\\|");

                        //部门名称
                        String deptName = arrayServices[0];

                        // 切分出部门下的各个微服务
                        // order//127.0.0.1:8091&127.0.0.1:8092|user//127.0.0.1:8091&127.0.0.1:8092
                        for (int i = 1; i < arrayServices.length; i++) {

                            // order//127.0.0.1:8091&127.0.0.1:8092
                            String arrayService = arrayServices[i];

                            String[] split = arrayService.split("//");
                            // 获得服务名
                            String serviceName = split[0];

                            // 服务地址：127.0.0.1:8091&127.0.0.1:8092
                            String servicesAddress = split[1];
                            String[] split1 = servicesAddress.split("&");
                            // 得到 ip和端口号
                            for (String s : split1) {
                                String[] ipAndPort = s.split(":");
                                services.add(ipAndPort);
                            }

                            mapAddress.put(deptName + "." + serviceName, services);
                        }

                    }
                }
            }
        }
        List<String[]> services = mapAddress.get(path);
        if (services == null || services.size() == 0) {
            throw new RuntimeException("没有可以提供服务的服务者");

        }
        RpcLoadBalancing rpcLoadBalancing = getRpcLoadBalancing();
        return rpcLoadBalancing.load(services, path);
    }

    /**
     * 简单工厂模式获取负载均衡实现
     *
     * @return
     */
    private RpcLoadBalancing getRpcLoadBalancing() {
        switch (loadbalancing) {
            case LoadBalancingConst.POLLING: {

                return BeanUtils.getBean(DefaultRpcLoadBalancing.class);
            }
            case LoadBalancingConst.RANDOM: {

                return BeanUtils.getBean(RandomRpcLoadBalancing.class);
            }
            case LoadBalancingConst.RESPONSE_TIME: {
                return BeanUtils.getBean(ResponseTimeRpcLoadBalancing.class);
            }
            default: {
                return BeanUtils.getBean(DefaultRpcLoadBalancing.class);

            }
        }
    }

    public String[] getHostAndPort() {
        if (arrayAddress == null) {
            synchronized (this) {
                if (arrayAddress == null) {
                    arrayAddress = new ArrayList<>();
                    String[] split = address.split(",");
                    for (String s : split) {
                        String[] split1 = s.split(":");
                        arrayAddress.add(split1);
                    }
                }
            }
        }
        // 此处负载均衡策略为随机
        int i = (int) (System.currentTimeMillis() % arrayAddress.size());
        return arrayAddress.get(i);
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getLoadbalancing() {
        return loadbalancing;
    }

    public void setLoadbalancing(String loadbalancing) {
        this.loadbalancing = loadbalancing;
    }

    public List<String[]> getArrayAddress() {
        return arrayAddress;
    }

    public void setArrayAddress(List<String[]> arrayAddress) {
        this.arrayAddress = arrayAddress;
    }

    public Map<String, List<String[]>> getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(Map<String, List<String[]>> mapAddress) {
        this.mapAddress = mapAddress;
    }
}

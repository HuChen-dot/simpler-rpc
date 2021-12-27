package org.hu.rpc.proxy;

import com.alibaba.fastjson.JSON;
import org.hu.rpc.annotation.RpcTag;
import org.hu.rpc.common.RpcRequest;
import org.hu.rpc.common.RpcResponse;
import org.hu.rpc.config.NettyClientConfig;
import org.hu.rpc.core.client.NettyRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/26 9:50 PM
 **/
@Component
//此注解用来开启下面的@ConfigurationProperties注解，
@EnableConfigurationProperties(JdkProxy.class)
//用来读取配置文件中的值，给类的属性自动赋值
@ConfigurationProperties(prefix = "simplerpc.netty.client.threadpoll")
public class JdkProxy {


    @Autowired
    private NettyClientConfig nettyClientConfig;

    public Object createProxy(Class clazz) {

        return Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                RpcRequest request = new RpcRequest();
                Class<?> declaringClass = method.getDeclaringClass();

                RpcTag rpcTag = declaringClass.getAnnotation(RpcTag.class);
                if (rpcTag == null) {
                    throw new RuntimeException("接口上没有定义：RpcTag 注解，无法区分你想要调用哪一个服务");
                }

                // 获取提供服务的机器的ip和端口
                String[] hostAndPort = nettyClientConfig.getHostAndPort(rpcTag.dept() + "." + rpcTag.service());

                // 设置请求标识
                request.setRequestId(UUID.randomUUID().toString());
                // 设置接口名称
                request.setClassName(declaringClass.getName());
                // 设置方法名
                request.setMethodName(method.getName());

                // 设置方法参数类型
                request.setParameterTypes(method.getParameterTypes());
                // 设置参数
                request.setParameters(args);
                // 发送消息
                NettyRpcClient nettyRpcClient = new NettyRpcClient(nettyClientConfig, hostAndPort);
                try {

                    String s = JSON.toJSONString(request);

                    RpcResponse send = nettyRpcClient.send(s);

                    if (send.getError() != null) {
                        throw new RuntimeException(send.getError());
                    }

                    Object result = send.getResult();
                    if (result == null) {
                        return result;
                    } else {
                        return JSON.parseObject(result.toString(), method.getReturnType());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 关闭资源
                    nettyRpcClient.close();
                }
                return null;
            }
        });


    }
}

package org.hu.rpc.zk.server;

import org.hu.rpc.annotation.RpcTag;
import org.hu.rpc.core.server.RpctServerHandler;
import org.hu.rpc.zk.util.IpUtils;
import org.hu.rpc.zk.util.ZkClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: hu.chen
 * @Description: 服务提供者初始化
 * @DateTime: 2021/12/29 1:15 PM
 **/
@Component
public class ServerInit {


    @Autowired
    private ZkClientUtils zkClientUtils;

    @Autowired
    private  RpctServerHandler rpctServerHandler;


    public void init(int port) {
        // 是否使用了 zk
        if (!zkClientUtils.isOpenzk()) {
            return;
        }

        // 获取根路径
        String namespace = zkClientUtils.getNamespace();

        // 判断根路径是否存在 / 的前缀，进行处理
        if (!namespace.startsWith("/")) {
            namespace = "/" + namespace;
        }
        // 判断根路径是否存在
        if (!zkClientUtils.exists(namespace)) {
            // 如果不存在 则创建节点
            zkClientUtils.createPersistent(namespace);
        }

        //获取当前服务的 ip
        String localIpAddr = IpUtils.getLocalIpAddr();

        List<Class> interfaceApi = rpctServerHandler.interfaceApi;


        for (Class aClass : interfaceApi) {
            RpcTag rpcTag = (RpcTag) aClass.getAnnotation(RpcTag.class);
            if (rpcTag == null) {
                throw new RuntimeException("接口上没有定义：RpcTag 注解，无法区分你想要调用哪一个服务");
            }

            //拼装当前节点微服务的节点路径
            String servicePath=namespace+"/"+rpcTag.dept() + "." + rpcTag.service();

            // 判断当前节点是否存在
            if(!zkClientUtils.exists(servicePath)) {
                // 在zk上创建当前微服务的节点信息
                zkClientUtils.createPersistent(servicePath);
            }
            String ipPath=servicePath + "/" + localIpAddr + ":" + port;

            //将当前服务的ip:端口，注册到根节点/当前微服务节点下,创建成临时节点
            zkClientUtils.createEphemeral(ipPath);
        }

    }


}

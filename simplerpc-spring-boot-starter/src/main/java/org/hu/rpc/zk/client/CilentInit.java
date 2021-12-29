package org.hu.rpc.zk.client;

import org.I0Itec.zkclient.IZkChildListener;
import org.hu.rpc.config.NettyClientConfig;
import org.hu.rpc.zk.util.ZkClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: hu.chen
 * @Description: 服务消费者启动初始化
 * @DateTime: 2021/12/29 1:36 PM
 **/
@Component
public class CilentInit {

    @Autowired
    private ZkClientUtils zkClientUtils;

    @Autowired
    private NettyClientConfig nettyClientConfig;

    @PostConstruct
    public void init() {
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

        Map<String, List<String[]>> mapAddress = nettyClientConfig.getMapAddress();

        List<String> childNodes = zkClientUtils.getNodes(namespace);
        for (String childNode : childNodes) {

            String path = namespace + "/" + childNode;

            List<String> ipNodes = zkClientUtils.getNodes(path);

            //给此节点创建监听，监听此节点下子节点列表的变化情况
            zkClientUtils.addNodeListener(path, new IZkChildListener() {
                @Override
                public void handleChildChange(String s, List<String> list) throws Exception {
                    Map<String, List<String[]>> mapAddress1 = nettyClientConfig.getMapAddress();
                    List<String[]> ipArray = new ArrayList<>();
                    for (String ipNode : list) {
                        ipArray.add(ipNode.split(":"));
                    }
                    mapAddress1.put(childNode, ipArray);
                }
            });


            List<String[]> ipArray = new ArrayList<>();
            for (String ipNode : ipNodes) {
                ipArray.add(ipNode.split(":"));
            }
            mapAddress.put(childNode, ipArray);
        }
    }
}

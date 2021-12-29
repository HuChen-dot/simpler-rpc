package org.hu.rpc.zk.util;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/29 10:15 AM
 **/
@Component
@EnableConfigurationProperties(ZkClientUtils.class)
@ConfigurationProperties(prefix = "simplerpc.netty.zk")
public class ZkClientUtils {

    private ZkClient zkClient;

    private String address ="127.0.0.1:2181";

    /**
     * 是否使用zk注册中心,默认关闭
     */
    private boolean openzk=false;

    /**
     * 服务注册的根节点
     */
    private String namespace="simplerpc";


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isOpenzk() {
        return openzk;
    }

    public void setOpenzk(boolean openzk) {
        this.openzk = openzk;
    }

    @PostConstruct
    public void init(){
        // 如果地址为空，则代表不使用zk注册中心
        if(!openzk){
            return;
        }
        /**
         * 连接会话
         * 创建一个zkClient实例就可以完成会话的连接
         * serverstring:连接的地址：ip:端口号
         */
        zkClient = new ZkClient(address);
    }


    /**
     * 创建持久节点，同时递归创建子节点
     * createParents:代表是否创建父节点，值为true代表先创建父节点在创建子节点，如果为false 代表只创建子节点
     */
    public  void createPersistent(String path) {
        zkClient.createPersistent(path, true);
    }

    /**
     * 创建持久节点，并给节点添加内容
     *
     * @param path
     * @param data
     */
    public  void createPersistent(String path, String data) {
        zkClient.createPersistent(path, data);
    }

    /**
     * 创建持久顺序节点，并给节点添加内容
     *
     * @param path
     * @param data
     */
    public  void createPersistentSequential(String path, String data) {
        zkClient.createPersistentSequential(path, data);
    }


    /**
     * 创建临时节点
     *
     * @param path 节点名称
     */
    public  void createEphemeral(String path) {
        zkClient.createEphemeral(path);
    }

    /**
     * 创建临时节点，并给节点添加内容
     *
     * @param path
     * @param data
     */
    public  void createEphemeral(String path, String data) {
        zkClient.createEphemeral(path, data);
    }

    /**
     * 创建临时顺序节点，并给节点添加内容
     *
     * @param path
     * @param data
     */
    public  void createEphemeralSequential(String path, String data) {
        zkClient.createEphemeralSequential(path, data);
    }

    /**
     * 删除节点：删除节点
     */
    public  void delete(String path) {
        zkClient.delete(path);
    }

    /**
     * 删除节点：递归删除节点，先删除该节点下的子节点，然后在删除该节点
     */
    public  void deleteRecursive(String path) {
        zkClient.deleteRecursive(path);
    }

    /**
     * 获取某节点的子节点列表
     */
    public  List<String> getNodes(String path) {
        List<String> nodes = zkClient.getChildren(path);
        return nodes;
    }

    /**
     * 给某个节点添加事件监听，当此节点的子节点列表发生变化时，会触发里面的 handleChildChange() 方法
     * 注意：原生的zkAPI的监听是一次性的,在监听触发后之前注册的监听就会失效，所以需要重新注册
     * 但是 ZkClient 实现了 反复注册监听的功能，所以再触发监听后不需要在重新注册
     *
     *
     *  new IZkChildListener() {
     *                     // s :代表当前监听节点的所有父节点，也就是路径
     *                     // list ：变化后的子节点列表
     *                     @Override
     *                     public void handleChildChange(String path, List<String> list) throws Exception {
     *                         System.err.println(path + ": 的子节点列表发生了变化，变化后的子节点列表为：" + list);
     *
     *
     *                     }
     *                 }
     */
    public  void addNodeListener(String path, IZkChildListener childListener) {
        zkClient.subscribeChildChanges(path,childListener);
    }

    /**
     * 给某个节点添加事件监听，当此节点的子节点列表发生变化时，会触发里面的 handleChildChange() 方法
     * 注意：设置监听是一次性的，在监听触发后之前注册的监听就会失效，所以需要重新注册
     *
     * new IZkDataListener() {
     *         // 当节点数据内容发生变化时，执行的方法
     *         // s:监听的节点
     *         // o :节点变化后的内容
     *         @Override
     *         public void handleDataChange (String s, Object o) throws Exception {
     *             System.err.println(s + "该节点内容被更新,更新后的内容：" + o);
     *
     *         }
     *
     *         // 当节点被删除时，执行的方法
     *         // s:监听的节点
     *         @Override
     *         public void handleDataDeleted (String s) throws Exception {
     *             System.err.println(s + "该节点内容被删除");
     *         }
     *     }
     */
    public  void addContentListener(String path, IZkDataListener dataListener) {
        zkClient.subscribeDataChanges(path,dataListener);
    }


    /**
     * 判断节点是否存在
     *
     * @param path
     * @return
     */
    public  boolean exists(String path) {

        return zkClient.exists(path);
    }

    /**
     * 读取节点内容
     *
     * @return
     */
    public  String readNode(String path) {
        Object o = zkClient.readData(path);
        return (String) o;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

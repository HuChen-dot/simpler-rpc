package org.hu.rpc.annotation;

import java.lang.annotation.*;

/**
 * @Author: hu.chen
 * @Description: 用来标识当前接口属于哪一个部门和部门中的哪一个服务
 * @DateTime: 2021/12/27 11:49 AM
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcTag {

    /**
     *  部门标识 （比如：人事部, 电商部, 企业效率部）
     * @return
     */
    String dept() default "";

    /**
     * 服务标识 （比如某一个部门下的：用户服务，订单服务，商品服务）
     * @return
     */
    String service() default "";


}

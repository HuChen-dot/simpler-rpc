package com.hu.api;


import com.hu.pojo.User;
import org.hu.rpc.annotation.RpcTag;

/**
 * 用户服务
 */
@RpcTag(dept = "xhs",service = "oa")
public interface IUserService {

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     */
    User getById(int id);
}

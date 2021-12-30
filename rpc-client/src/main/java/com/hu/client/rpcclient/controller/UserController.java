package com.hu.client.rpcclient.controller;

import com.hu.client.rpcclient.service.impl.OrderService0;
import com.hu.client.rpcclient.service.impl.UserServiceImpl;
import com.hu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hu.chen
 * @Description:
 * @DateTime: 2021/12/26 6:52 PM
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderService0 orderService0;


    @GetMapping("/getUserById")
    public String getUserById(Integer id) {

        User user = userService.getById(id);
        if(user==null){
            return "null";
        }
        return user.toString();
    }

    @GetMapping("/getOrderById")
    public String getOrderById(Integer id) {

        return orderService0.find(id);
    }
}

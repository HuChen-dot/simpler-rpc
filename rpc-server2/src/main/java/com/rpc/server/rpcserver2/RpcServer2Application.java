package com.rpc.server.rpcserver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcServer2Application {

    public static void main(String[] args) {
        SpringApplication.run(RpcServer2Application.class, args);
        System.err.println("服务提供者2启动");
    }

}

package com.dingjiaxiong.example.provider;

import com.dingjiaxiong.RpcApplication;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.registry.LocalRegistry;
import com.dingjiaxiong.xiongrpc.server.HttpServer;
import com.dingjiaxiong.xiongrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 * @author Ding Jiaxiong
 */
public class EasyProviderExample {

    public static void main(String[] args) {


        //Rpc 框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}

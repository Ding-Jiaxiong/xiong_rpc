package com.dingjiaxiong.example.provider;


import com.dingjiaxiong.RpcApplication;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.bootstrap.ProviderBootstrap;
import com.dingjiaxiong.xiongrpc.config.RegistryConfig;
import com.dingjiaxiong.xiongrpc.config.RpcConfig;
import com.dingjiaxiong.xiongrpc.model.ServiceMetaInfo;
import com.dingjiaxiong.xiongrpc.model.ServiceRegisterInfo;
import com.dingjiaxiong.xiongrpc.registry.LocalRegistry;
import com.dingjiaxiong.xiongrpc.registry.Registry;
import com.dingjiaxiong.xiongrpc.registry.RegistryFactory;
import com.dingjiaxiong.xiongrpc.server.VertxHttpServer;
import com.dingjiaxiong.xiongrpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 *
 * @author Ding Jiaxiong
 */
public class ProviderExample {

    public static void main(String[] args) {

        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);

//        // PRC 框架初始化
//        RpcApplication.init();
//
//        // 注册服务
//        String serviceName = UserService.class.getName();
//        LocalRegistry.register(serviceName, UserServiceImpl.class);
//
//        // 注册服务到注册中心
//        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
//
//        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
//
//        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
//
//        serviceMetaInfo.setServiceName(serviceName);
//        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
//        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
//
//        try {
//
//            registry.register(serviceMetaInfo);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
////        // 启动 web 服务
////        VertxHttpServer httpServer = new VertxHttpServer();
////        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
//
//        // 启动 TCP 服务
//        VertxTcpServer vertxTcpServer = new VertxTcpServer();
//        vertxTcpServer.doStart(rpcConfig.getServerPort());

    }
}

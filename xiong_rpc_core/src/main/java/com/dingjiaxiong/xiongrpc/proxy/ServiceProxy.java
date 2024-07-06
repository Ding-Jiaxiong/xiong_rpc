package com.dingjiaxiong.xiongrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dingjiaxiong.RpcApplication;
import com.dingjiaxiong.xiongrpc.config.RpcConfig;
import com.dingjiaxiong.xiongrpc.constant.RpcConstant;
import com.dingjiaxiong.xiongrpc.fault.retry.RetryStrategy;
import com.dingjiaxiong.xiongrpc.fault.retry.RetryStrategyFactory;
import com.dingjiaxiong.xiongrpc.fault.tolerant.TolerantStrategy;
import com.dingjiaxiong.xiongrpc.fault.tolerant.TolerantStrategyFactory;
import com.dingjiaxiong.xiongrpc.loadbalancer.LoadBalancer;
import com.dingjiaxiong.xiongrpc.loadbalancer.LoadBalancerFactory;
import com.dingjiaxiong.xiongrpc.model.RpcRequest;
import com.dingjiaxiong.xiongrpc.model.RpcResponse;
import com.dingjiaxiong.xiongrpc.model.ServiceMetaInfo;
import com.dingjiaxiong.xiongrpc.protocol.*;
import com.dingjiaxiong.xiongrpc.registry.Registry;
import com.dingjiaxiong.xiongrpc.registry.RegistryFactory;
import com.dingjiaxiong.xiongrpc.serializer.JdkSerializer;
import com.dingjiaxiong.xiongrpc.serializer.Serializer;
import com.dingjiaxiong.xiongrpc.serializer.SerializerFactory;
import com.dingjiaxiong.xiongrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 *
 * @author Ding Jiaxiong
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());


        // 构造请求

        String serviceName = method.getDeclaringClass().getName();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 暂时先取第一个
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);


        // 2024年7月6日11:11:51 采用负载均衡
        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        System.out.println("这次请求的端口是" + selectedServiceMetaInfo.getServicePort());

        //发送TCP 请求【rpc 请求】


        // 2024年7月6日11:51:32 使用重试机制
//            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());

        // 2024年7月6日10:42:07
//            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);

        // 使用容错机制
        // 2024年7月6日12:28:10 使用重试机制
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception e) {
            // 容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }


        return rpcResponse.getData();


//            Vertx vertx = Vertx.vertx();
//            NetClient netClient = vertx.createNetClient();
//
//            CompletableFuture<Object> responseFuture = new CompletableFuture<>();
//
//            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(), result -> {
//                if (result.succeeded()) {
//                    System.out.println("Connected to TCP server.");
//
//                    io.vertx.core.net.NetSocket socket = result.result();
//
//                    // 发送数据
//                    // 构造消息
//
//                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                    ProtocolMessage.Header header = new ProtocolMessage.Header();
//
//                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                    header.setRequestId(IdUtil.getSnowflakeNextId());
//
//                    protocolMessage.setHeader(header);
//                    protocolMessage.setBody(rpcRequest);
//
//                    // 编码请求
//                    try {
//
//                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
//                        socket.write(encodeBuffer);
//                    } catch (Exception e) {
//                        throw new RuntimeException("协议消息编码错误");
//                    }
//
//                    // 接收响应
//                    socket.handler(buffer -> {
//                        try {
//
//                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                            responseFuture.complete(rpcResponseProtocolMessage.getBody());
//                        } catch (IOException e) {
//                            throw new RuntimeException("协议消息解码错误");
//                        }
//                    });
//
//                } else {
//
//                    System.err.println("Failed to connect to TCP server.");
//                }
//            });
//
//
//            RpcResponse rpcResponse = (RpcResponse) responseFuture.get();
//
//            // 记得关闭连接
//            netClient.close();
//            return rpcResponse.getData();
//
////            // 发送请求
////            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
////            // 2024年7月5日20:07:20 解决
////            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
////                    .body(bodyBytes)
////                    .execute()) {
////                byte[] result = httpResponse.bodyBytes();
////                // 反序列化
////                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
////                return rpcResponse.getData();
////            }

    }
}

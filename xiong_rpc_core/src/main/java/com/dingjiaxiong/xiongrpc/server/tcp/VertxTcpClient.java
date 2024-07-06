package com.dingjiaxiong.xiongrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.dingjiaxiong.RpcApplication;
import com.dingjiaxiong.xiongrpc.model.RpcRequest;
import com.dingjiaxiong.xiongrpc.model.RpcResponse;
import com.dingjiaxiong.xiongrpc.model.ServiceMetaInfo;
import com.dingjiaxiong.xiongrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 请求客户端
 *
 * @author Ding Jiaxiong
 */
public class VertxTcpClient {

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @param serviceMetaInfo
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException {
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (!result.succeeded()) {
                        System.err.println("Failed to connect to TCP server");
                        return;
                    }
                    NetSocket socket = result.result();
                    // 发送数据
                    // 构造消息
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    // 生成全局请求 ID
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);

                    // 编码请求
                    try {
                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(encodeBuffer);
                    } catch (IOException e) {
                        throw new RuntimeException("协议消息编码错误");
                    }

                    // 接收响应
                    TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                            buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                            (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            }
                    );
                    socket.handler(bufferHandlerWrapper);

                });

        RpcResponse rpcResponse = responseFuture.get();
        // 记得关闭连接
        netClient.close();
        return rpcResponse;
    }


//    public void start() {
//
//        // 创建Vert.x 实例
//        Vertx vertx = Vertx.vertx();
//
//        vertx.createNetClient().connect(8888, "localhost", result -> {
//            if (result.succeeded()) {
//
//                System.out.println("Connected to TCP server");
//                io.vertx.core.net.NetSocket socket = result.result();
//
////                // 发送数据
////                socket.write("Hello, server!");
//
//                // 测试
//                for (int i = 0; i < 1000; i++) {
////                    socket.write("Hello, server! Hello, server!Hello, server!Hello, server!");
//
//
//                    // 发送数据
//                    Buffer buffer = Buffer.buffer();
//                    String str = "Hello, server! Hello, server!Hello, server!Hello, server!";
//
//                    buffer.appendInt(0);
//                    buffer.appendInt(str.getBytes().length);
//                    buffer.appendBytes(str.getBytes());
//                    socket.write(buffer);
//
//                }
//
//                // 接收响应
//                socket.handler(buffer -> {
//                    System.out.println("Received response from server: " + buffer.toString());
//                });
//            } else {
//                System.out.println("Failed to connect to TCP server");
//            }
//        });
//    }

//    public static void main(String[] args) {
//
//        new VertxTcpClient().start();
//
//    }

}

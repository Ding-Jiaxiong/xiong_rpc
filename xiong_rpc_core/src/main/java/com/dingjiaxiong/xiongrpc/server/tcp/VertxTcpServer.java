package com.dingjiaxiong.xiongrpc.server.tcp;

import com.dingjiaxiong.xiongrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * Vertx TCP 服务器
 *
 * @author Ding Jiaxiong
 */
@Slf4j
public class VertxTcpServer implements HttpServer {


    private byte[] handleRequest(byte[] requestData) {

        // 在这里编写处理请求的逻辑，根据requestData构造响应数据并返回
        ////这里只是一个示例，实际逻辑需要根据具体的业务需求来实现

        return "Hello, client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        server.connectHandler(new TcpServerHandler());

        // 处理请求
//        server.connectHandler(socket -> {
//
//            // 处理链接
////            socket.handler(buffer -> {
//
////                System.out.println("Received response from client: " + buffer.toString());
//
////            String testMessage = "Hello, server! Hello, server!Hello, server!Hello, server!";
////
////            int messageLength = testMessage.getBytes().length;
//
//            // 构造parser
//            RecordParser parser = RecordParser.newFixed(8);
//            parser.setOutput(new Handler<Buffer>() {
//
//                // 初始化
//                int size = -1;
//                // 一次完整的读取 ( 头 + 体 )
//                Buffer resultBuffer = Buffer.buffer();
//
//                @Override
//                public void handle(Buffer buffer) {
//
//                    if (-1 == size) {
//                        // 读取消息体长度
//                        size = buffer.getInt(4);
//                        parser.fixedSizeMode(size);
//
//                        // 写入头信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                    } else {
//                        // 写入体信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                        System.out.println(resultBuffer.toString());
//
//                        // 重置一轮
//                        parser.fixedSizeMode(8);
//                        size = -1;
//                        resultBuffer = Buffer.buffer();
//                    }
//
//
//
////                    String str = new String(buffer.getBytes());
////
////                    System.out.println(str);
////
////                    if (testMessage.equals(str)) {
////                        System.out.println("Good.");
////                    }
//                }
////                });
//
////                if (buffer.getBytes().length < messageLength) {
////                    System.out.println("半包, length = " + buffer.getBytes().length);
////                    return;
////                }
////
////                if (buffer.getBytes().length > messageLength) {
////                    System.out.println("粘包, length = " + buffer.getBytes().length);
////                    return;
////                }
////
////                String str = new String(buffer.getBytes(0, messageLength));
////                System.out.println(str);
////
////                if (testMessage.equals(str)) {
////                    System.out.println("Good.");
////                }
//
////                // 处理接收到的字节数组
////                byte[] requestData = buffer.getBytes();
////
////                //在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造响应等
////                byte[] reponseData = handleRequest(requestData);
////
////                // 发送响应
////                socket.write(Buffer.buffer(reponseData));
//
//            });
//
//            socket.handler(parser);
//
//        });

        // 启动 TCP 服务器并监听指定端口

        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port " + port);
            } else {
                log.info("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}

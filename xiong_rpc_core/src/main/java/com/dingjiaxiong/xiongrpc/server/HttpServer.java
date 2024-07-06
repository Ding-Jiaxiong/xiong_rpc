package com.dingjiaxiong.xiongrpc.server;

/**
 * HTTP 服务器接口
 *
 * @author Ding Jiaxiong
 */
public interface HttpServer {

    /**
     * 启动服务器
     *
     * @param port
     */
    void doStart(int port);

}

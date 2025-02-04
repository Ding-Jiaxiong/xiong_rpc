package com.dingjiaxiong.xiongrpc.config;

import com.dingjiaxiong.xiongrpc.fault.retry.RetryStrategyKeys;
import com.dingjiaxiong.xiongrpc.fault.tolerant.TolerantStrategyKeys;
import com.dingjiaxiong.xiongrpc.loadbalancer.LoadBalancerKeys;
import com.dingjiaxiong.xiongrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC 框架全局配置
 *
 * @author Ding Jiaxiong
 */
@Data
public class RpcConfig {

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 名称
     */
    private String name = "xiong-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}

package com.dingjiaxiong.xiongrpc.fault.tolerant;

import com.dingjiaxiong.xiongrpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 *
 * @author Ding Jiaxiong
 */

public interface TolerantStrategy {

    /**
     * 容错
     *
     * @param context 上下文，用于传递数据
     * @param e       异常
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}

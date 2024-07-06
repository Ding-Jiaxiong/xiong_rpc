package com.dingjiaxiong.example.consumer;


import com.dingjiaxiong.example.common.model.User;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.bootstrap.ConsumerBootstrap;
import com.dingjiaxiong.xiongrpc.config.RpcConfig;
import com.dingjiaxiong.xiongrpc.proxy.ServiceProxyFactory;
import com.dingjiaxiong.xiongrpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 *
 * @author Ding Jiaxiong
 */
public class ConsumerExample {

    public static void main(String[] args) {


        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }



//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//
//        System.out.println(rpc);


//        for (int i = 0; i < 3; i++) {
//            // 获取代理
//            UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//
//            User user = new User();
//            user.setName("dingjiaxiong");
//
//            // 调用
//            User newUser = userService.getUser(user);
//
//            if (newUser != null) {
//                System.out.println(newUser.getName());
//            } else {
//                System.out.println("user == null");
//            }
//
//            long number = userService.getNumber();
//            System.out.println(number);
//
//            System.out.printf("完成第 %d 次调用." , i + 1 );
//        }

    }
}

package com.dingjiaxiong.example.consumer;


import com.dingjiaxiong.example.common.model.User;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 *
 * @author Ding Jiaxiong
 */
public class EasyConsumerExample {

    public static void main(String[] args) {

//        // 静态代理
//        UserService userService = new UserServiceProxy();

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("jiaxiong");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}

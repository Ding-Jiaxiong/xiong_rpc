package com.dingjiaxiong.example_springboot_consumer;

import com.dingjiaxiong.example.common.model.User;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * 示例服务实现类
 *
 * @author Ding Jiaxiong
 */
@Service
public class ExampleServiceImpl {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcReference
    private UserService userService;

    /**
     * 测试方法
     */
    public void test() {
        User user = new User();
        user.setName("dingjiaxiong");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}

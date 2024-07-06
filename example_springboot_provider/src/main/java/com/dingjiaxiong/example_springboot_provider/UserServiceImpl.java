package com.dingjiaxiong.example_springboot_provider;

import com.dingjiaxiong.example.common.model.User;
import com.dingjiaxiong.example.common.service.UserService;
import com.dingjiaxiong.xiongrpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author Ding Jiaxiong
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}

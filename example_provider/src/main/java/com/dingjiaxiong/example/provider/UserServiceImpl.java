package com.dingjiaxiong.example.provider;

import com.dingjiaxiong.example.common.model.User;
import com.dingjiaxiong.example.common.service.UserService;

/**
 * 用户服务实现类
 *
 * @author Ding Jiaxiong
 */
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}

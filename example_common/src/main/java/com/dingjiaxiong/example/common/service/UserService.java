package com.dingjiaxiong.example.common.service;

import com.dingjiaxiong.example.common.model.User;

/**
 * 用户服务
 *
 * @author Ding Jiaxiong
 */

public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * 用于测试 mock 接口返回值
     *
     * @return
     */
    default short getNumber() {
        return 1;
    }

}
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by fanlinglong on 2018/1/6.
 */
public interface IUserService {

    ServerResponse<User> login(String username,String password);
}

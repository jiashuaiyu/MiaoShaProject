package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.UserModel;

public interface UserService {

//    通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);
//    处理用户注册的请求
    void register(UserModel userModel) throws BusinessException;
}

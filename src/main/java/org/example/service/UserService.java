package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.UserModel;

public interface UserService {

//    通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);
//    处理用户注册的请求
    void register(UserModel userModel) throws BusinessException;

//    验证登录
//    telephone：用户注册手机
//    password：用户加密后的密码
    UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}

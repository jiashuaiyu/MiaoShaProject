package org.example.controller;

import org.example.controller.viewobject.UserVO;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@Controller("user")用来指定controller的标记，使得它能被spring扫描到，这个controller标记的名字叫user
//@RequestMapping("/user")  使得这个可以在浏览器上通过/user被访问到
@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
//        调用service服务获取对应的id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

//        若获取的用户信息不存在
        if(userModel == null)
        {
//            错误被抛到了tomcat容器层
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

//        将核心领域模型用户对象转化为可供用户使用的viewobject
        UserVO userVO = convertFormUserModel(userModel);

//        返回通用对象
//        通过CommonReturnType进行返回值错误归一化判断，并进行处理
        return CommonReturnType.creat(userVO);
    }

    private UserVO convertFormUserModel(UserModel userModel)
    {
        if(userModel==null)
        {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

//    定义exceptionHandler解决未被controller层吸收的exception
//    @ExceptionHandler()需要设置发生什么样的错误之后就可以进入他的环节，此处设置的是Exception根类
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception ex){

        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");
        commonReturnType.setData(ex);
        return commonReturnType;
    }
}

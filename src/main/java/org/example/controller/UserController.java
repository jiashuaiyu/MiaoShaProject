package org.example.controller;

import org.example.controller.viewobject.UserVO;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller("user")用来指定controller的标记，使得它能被spring扫描到，这个controller标记的名字叫user
//@RequestMapping("/user")  使得这个可以在浏览器上通过/user被访问到
@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public UserVO getUser(@RequestParam(name = "id")Integer id)
    {
//        调用service服务获取对应的id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
//        将核心领域模型用户对象转化为可供用户使用的viewobject
        return convertFormUserModel(userModel);
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
}

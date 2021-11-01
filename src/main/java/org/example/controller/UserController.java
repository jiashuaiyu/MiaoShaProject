package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller("user")用来指定controller的标记，使得它能被spring扫描到，这个controller标记的名字叫user
//@RequestMapping("/user")  使得这个可以在浏览器上通过/user被访问到
@Controller("user")
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/get")
    public void getUser(@RequestParam(name = "id")Integer id)
    {
//        调用service服务获取对应的id的用户对象并返回给前端
    }

}

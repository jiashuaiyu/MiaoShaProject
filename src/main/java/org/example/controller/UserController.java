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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//@Controller("user")用来指定controller的标记，使得它能被spring扫描到，这个controller标记的名字叫user
//@RequestMapping("/user")  使得这个可以在浏览器上通过/user被访问到
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


//    用户获取OPT短信接口
    @RequestMapping("/getopt")
    @ResponseBody
    public CommonReturnType getOpt(@RequestParam(name = "telephone")String telephone){

//        分为三步
//        1. 需要按照一定的规则生成OPT验证码，本初采用随机数的生成方式
        Random random = new Random();
//        此处生成0~99999的随机数
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);


//        2、 将OPT验证码同对应的手机号关联,此处使用httpSession的方式绑定他的手机号与OTPCODE
//            这里httpServletRequest对应当前用户的http请求
        /**
         * (1)request.setAttribute("num",value)；
         *    有效范围是一个请求范围，不发送请求的界面无法获取到value的值
         * (2)request.getSession().setAttribute("num",value)；
         *   有效范围是一个session周期，在session过期之前或者用户关闭页面之前是有效的
         *
         * httpServletRequest也就是我们常说的request，
         * httpServletRequest.setAttribute意思就是在request范围内设置一个属性主要用来存值供其他页面操作，
         * setAttribute("name",value)有两个参数第一个是由你定义的名称，
         * 第二个是要存入的值,在相邻页面你可以用 httpServletRequest.getAttribute("name")获取到value
         */
        httpServletRequest.getSession().setAttribute(telephone, optCode);


//        3. 将OPT验证码通过短信通道发送给用户（此处省略，这里可以采用阿里云等短信方式）
//           此处将optCode打印到控制台
        System.out.println("telephone = "+ telephone + " & optCode = "+optCode);

        return CommonReturnType.creat(null);


    }




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
//            userModel.setEncrptPassword("123");
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


}

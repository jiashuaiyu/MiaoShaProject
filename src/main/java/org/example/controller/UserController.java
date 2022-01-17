package org.example.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.example.controller.viewobject.UserVO;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Encoder;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Controller("user")用来指定controller的标记，使得它能被spring扫描到，这个controller标记的名字叫user
 * @RequestMapping("/user")  使得这个可以在浏览器上通过/user被访问到
 * @CrossOrigin 通过这个注解就可以完成所有的springboot对应返回web请求当中加上跨域allow-head标签
 * @CrossOrigin无法做到Session共享
 * DEFAULT_ALLOWED_HEADERS:允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
 *
 */
//@CrossOrigin(allowCredentials =  "true", allowedHeaders = "*")
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials =  "true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登陆接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验,判断手机号和密码不为空
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone, this.EncodeByMd5(password));

//        将登陆凭证加入到用户登录成功地Session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.creat(null);


    }



//    用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

//        验证手机号和用户对应的otpCode是否相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(inSessionOtpCode, otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }

//        如果当前用户的otpCode是合法的，则进入用户注册流程
//        用户注册流程中，需要有一个service去处理对应用户注册的请求

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.creat(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();

        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;


    }





//    用户获取OPT短信接口
    @RequestMapping(value = "/getopt", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOpt(@RequestParam(name = "telephone")String telephone){

//        分为三步
//        1. 需要按照一定的规则生成OPT验证码，本初采用随机数的生成方式
        Random random = new Random();
//        此处生成0~99999的随机数
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);


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
        httpServletRequest.getSession().setAttribute(telephone, otpCode);


//        3. 将OPT验证码通过短信通道发送给用户（此处省略，这里可以采用阿里云等短信方式）
//           此处将otpCode打印到控制台
        System.out.println("telephone = "+ telephone + " & otpCode = "+otpCode);

//        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", request.getSession().getId())
//                .httpOnly(true)
//                .secure(true)
//                .domain("localhost")
//                .path("/")
//                .maxAge(3600)
//                .sameSite("None")
//                .build()
//                ;
//        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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

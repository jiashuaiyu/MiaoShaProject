package org.example.controller;

import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";



    //    定义exceptionHandler解决未被controller层吸收的exception
//    @ExceptionHandler()需要设置发生什么样的错误之后就可以进入他的环节，此处设置的是Exception根类
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){

        Map<String, Object> responseData = new HashMap<>();

//        如果ex属于BusinessException错误，否则
        if(ex instanceof BusinessException)
        {
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.creat(responseData,"fail");

//        CommonReturnType commonReturnType = new CommonReturnType();
//        commonReturnType.setStatus("fail");
//        commonReturnType.setData(responseData);
////        commonReturnType.setData(ex);
//        return commonReturnType;
    }
}

package org.example.validator;

import com.alibaba.druid.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ShuaiYu_Jia
 * @Data: 2022/1/17
 * @Description:
 */
public class ValidationResult {
    //    校验结果是否有错
    private boolean hasErrors = false;

    //    存放错误信息的map
    private Map<String, String> errorMsgMap = new HashMap<>();


    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

//    实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrMsg(){
//        都叫StringUtils但源码不一样
        return org.apache.commons.lang3.StringUtils.join(errorMsgMap.values().toArray(),",");

    }
}

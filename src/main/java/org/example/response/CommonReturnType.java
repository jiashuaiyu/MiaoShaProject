package org.example.response;

public class CommonReturnType {

//    表明对应请求返回的处理结果，有“success”和“fail”
    private String status;

//    若status==success,则data内返回前端需要的json数据
//    若status==fail,则data内使用通用的错误码格式
    private Object data;

//    定义一个通用的创建方法
//    利用重载
//    如果状态码是200,则状态码默认省略不带状态码，不是200则会带状态码
    public static CommonReturnType creat(Object result)
    {
        return CommonReturnType.creat(result,"success");
    }

    public static CommonReturnType creat(Object result, String status)
    {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

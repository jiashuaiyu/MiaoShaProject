package org.example.service.model;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//UserModel的model层面是真正意义上我们处理业务的核心模型
//而dataobject仅仅是数据库的一个映射
public class UserModel {

//    用户对象的信息字段，和数据库中对应
    private Integer id;

//    @NotBlank表示name不能为null字符串，也不能为null。否则报message错误
    @NotBlank(message = "用户名不能为空")
    private String name;

//    @NotNull表示字段不能为null，否则报message错误
    @NotNull(message = "性别不能不填")
    private Byte gender;

    @NotNull(message = "年龄不能不填写")
    @Min(value = 0, message = "年龄必须大于0")   //规定年龄最小值
    @Max(value = 150, message = "年龄必须小于于150")   //规定年龄最大值
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telphone;
    private String registerMode;
    private String thirdPartId;

    @NotBlank(message = "密码不能为空")
    private String encrptPassword;



    /**
     * java中 当定义了一个私有的成员变量的时候，如果需要访问或者获取这个变量的时候，就可以编写set或者get方法去调用。
     *
     * set()是给属性赋值的，get()是取得属性值的
     * @return
     */

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartId() {
        return thirdPartId;
    }

    public void setThirdPartId(String thirdPartId) {
        this.thirdPartId = thirdPartId;
    }
}

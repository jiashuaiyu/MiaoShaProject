package org.example.service.impl;

import com.alibaba.druid.util.StringUtils;
import org.example.dao.UserDOMapper;
import org.example.dao.UserPasswordDOMapper;
import org.example.dataobject.UserDO;
import org.example.dataobject.UserPasswordDO;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//@Service通过这个标记指明UserServiceImpl是我们的一个spring service
@Service
public class UserServiceImpl implements UserService {


//    引入UserDOMapper类
    @Autowired
    private UserDOMapper userDOMapper;

//    引入UserPasswordDOMapper
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

//    覆盖方法
    @Override
    public UserModel getUserById(Integer id){

//        调用userDOMapper获取到用户对应的dataObject
//        根据主键id获取到userDo的对象
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO==null)
        {
            return null;
        }
//        这个方法不是配置文件自动生成的，我们要自行配置xml文件和dao包中的文件
//        通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);



    }

//    实现用户注册流程的处理
//    @Transactional 是声明式事务管理

    /**
     * @Transactional 可以作用于接口、接口方法、类以及类方法上。
     * 当作用于类上时，该类的所有 public 方法将都具有该类型的事务属性，
     * 同时，我们也可以在方法级别使用该标注来覆盖类级别的定义。
     *
     * @Transactional 注解应该只被应用到 public 方法上，如果你在 protected、private 或者默认可见性的方法上使用 @Transactional 注解，这将被忽略，也不会抛出任何异常。
     * @param userModel
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null)
        {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

//        用apache-commons做校验
        if(StringUtils.isEmpty(userModel.getName())   //String判null
            || userModel.getAge()==null   // 数字判null
            || userModel.getGender()==null
            || StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }



//        实现model->dataobject方法
        UserDO userDO = convertFormModel(userModel);
        try{
            //        将用户信息插入数据库
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }


        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFormModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }


    private UserPasswordDO convertPasswordFormModel(UserModel userModel){
        if(userModel == null)
        {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }


    private UserDO convertFormModel(UserModel userModel)
    {
        if(userModel==null)
        {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }



    //    封装一个userModel出来
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){

        if(userDO==null)
        {
            return null;
        }
        UserModel userModel = new UserModel();

//        使用org.springframework.beans.BeanUtils.copyProperties方法进行对象之间属性的赋值，
//        避免通过get、set方法一个一个属性的赋值
//        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(userDO, userModel);

        if(userPasswordDO != null)
        {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }


        return userModel;
    }
}

package org.example.service.impl;

import org.example.dao.UserDOMapper;
import org.example.dao.UserPasswordDOMapper;
import org.example.dataobject.UserDO;
import org.example.dataobject.UserPasswordDO;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

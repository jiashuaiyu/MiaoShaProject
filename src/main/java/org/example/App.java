package org.example;

import org.example.dao.UserDOMapper;
import org.example.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

//把App启动类类当做一个springboot的自动化配置bean,并且能够开启整个工程类基于springboot的自动化配置
//@EnableAutoConfiguration

//@SpringBootApplication与@EnableAutoConfiguration异曲同工，都是将APP类被spring托管，
//并且指定他是一个主启动类，
@SpringBootApplication(scanBasePackages = {"org.example"})
@RestController
@MapperScan("org.example.dao")//要把Dao存放的地方设置在这个注解下
public class App 
{
    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping
    public String home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if(userDO == null)
        {
            return "用户不存在";
        }else {
            return userDO.getName();
        }
//        return "hello world";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

//        这行代码用于启动springboot项目
        SpringApplication.run(App.class,args);
    }
}

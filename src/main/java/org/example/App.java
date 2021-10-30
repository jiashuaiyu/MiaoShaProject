package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

//把App启动类类当做一个springboot的自动化配置bean,并且能够开启整个工程类基于springboot的自动化配置
@EnableAutoConfiguration
@RestController
public class App 
{
    @RequestMapping
    public String home(){
        return "hello world";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

//        这行代码用于启动springboot项目
        SpringApplication.run(App.class,args);
    }
}
